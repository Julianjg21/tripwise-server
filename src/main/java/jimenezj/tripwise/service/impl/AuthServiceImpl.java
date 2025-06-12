package jimenezj.tripwise.service.impl;

import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.dto.user.UserProfileResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private RefreshTokenRepository refreshTokenRepository;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            // Tries to authenticate the user with the provided credentials
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
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

        return new AuthResponse(accessToken, refreshToken);
    }

    // Stores new refresh token and removes previous one (if any)
    private void saveRefreshToken(User user, String tokenString) {
        refreshTokenRepository.findByUser_Id(user.getId()).ifPresent(refreshTokenRepository::delete);

        long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7 days

        RefreshToken newRefreshToken = new RefreshToken(tokenString, Instant.now().plusMillis(refreshTokenValidity), user);
        refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public void signup(SignupRequest request) throws BadRequestException {
        // Check if email already exists
        if (userRepository.existsByEmail((request.email()))) {
            throw new BadRequestException("El correo ya está registrado");
        }

        // Create new user with hashed password
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setFullName(request.fullName());
        newUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(newUser);
    }

    @Override
    public AuthRefreshResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.token();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    // New access token, same refresh token
                    String accessToken = jwtUtils.generateAccessToken(user);
                    return new AuthRefreshResponse(accessToken, requestRefreshToken);
                }).orElseThrow(() -> new BadRequestException("Invalid refresh token"));
    }

    // If token is expired, delete it and throw exception
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    public UserProfileResponse getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User not authenticated");
        }

        // Extract user from security context and return profile info
        UserDetailsImpl user =  (UserDetailsImpl) authentication.getPrincipal();
        return new UserProfileResponse(user.getId(), user.getEmail(), user.getFullName());
    }
}
