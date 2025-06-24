package jimenezj.tripwise.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.exception.BadRequestException;
import jimenezj.tripwise.model.RefreshToken;
import jimenezj.tripwise.model.User;
import jimenezj.tripwise.repository.RefreshTokenRepository;
import jimenezj.tripwise.repository.UserRepository;
import jimenezj.tripwise.security.impl.UserDetailsImpl;
import jimenezj.tripwise.security.jwt.JwtUtils;
import jimenezj.tripwise.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;

    // Injecting dependencies
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
            AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AuthTokens login(LoginRequest request) {
        Authentication authentication;
        try {
            // Tries to authenticate the user with the provided credentials
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            // Invalid credentials
            throw new BadCredentialsException("Incorrect mail or password");
        }

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Create access and refresh tokens
        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        // Save refresh token in DB
        saveRefreshToken(user, refreshToken);

        // Return the tokens wrapped in AuthTokens object
        return new AuthTokens(accessToken, refreshToken);
    }

    /**
     * Replaces any existing refresh token for the given user with a new one.
     * If a previous token exists, it is deleted before saving the new one.
     */
    private void saveRefreshToken(User user, String tokenString) {
        // Delete any existing refresh token for the user
        refreshTokenRepository.findByUser_Id(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        // Set refresh token validity duration: 7 days (in milliseconds)
        long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7;

        // Create and store the new refresh token with expiration
        RefreshToken newRefreshToken = new RefreshToken(
                tokenString,
                Instant.now().plusMillis(refreshTokenValidity),
                user);

        refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public void signup(SignupRequest request) throws BadRequestException {
        // Check if email already exists
        if (userRepository.existsByEmail((request.email()))) {
            throw new BadRequestException("The mail is already registered");
        }

        // Create new user with hashed password
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);// Save the new user to the database
    }

    @Override
    public AuthTokens refreshToken(HttpServletRequest request) {
        String requestRefreshToken = extractRefreshTokenFromCookies(request);

        return refreshTokenRepository.findByToken(requestRefreshToken) // Look for the refresh token in the database
                .map(this::verifyExpiration) // Ensure the token hasn't expired
                .map(RefreshToken::getUser) // Get the user associated with the token
                .map(user -> {
                    // Wrap the user in a UserDetails implementation
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);

                    // Generate a new access token for the user
                    String accessToken = jwtUtils.generateAccessToken(userDetails);

                    // Return the new access token along with the same refresh token
                    return new AuthTokens(accessToken, requestRefreshToken);
                })
                .orElseThrow(() -> new BadRequestException("Invalid refresh token")); // If token is not found, throw error

    }

    // Extracts the refresh token from the HttpOnly cookie in the request
    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        // Check if the request has cookies and find the refresh token cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue(); // Return the value of the refresh token cookie
                }
            }
        }
        // If no refresh token cookie is found, throw an exception
        throw new BadRequestException("Refresh token cookie not found");
    }

    // If token is expired, delete it and throw exception
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            // If the token is expired, delete it from the repository
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token has expired. Please log in again.");
        }
        return token;// If the token is valid, return it
    }

}
