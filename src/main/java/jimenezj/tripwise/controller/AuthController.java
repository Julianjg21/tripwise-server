package jimenezj.tripwise.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.service.AuthService;
import jimenezj.tripwise.service.CsrfService;
import jimenezj.tripwise.service.HttpOnlyCookieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final HttpOnlyCookieService onlyCookieService;
    private final CsrfService csrfService;

    // Injecting dependencies
    public AuthController(AuthService authService, HttpOnlyCookieService onlyCookieService, CsrfService csrfService) {
        this.authService = authService;
        this.onlyCookieService = onlyCookieService;
        this.csrfService = csrfService;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {

        //Validate credentials and generate tokens
        AuthTokens tokens = authService.login(request);

        // Add the refresh token to the HttpOnly cookie
        onlyCookieService.addRefreshTokenCookie(response, tokens.refreshToken());

        //Generate CSRF token and add it to the response
        String csrfToken = csrfService.generateCsrfToken();
        csrfService.addCsrfTokenCookie(response, csrfToken);

        // Return the access token in the response body
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken()));
    }

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully registered user.");

    }

    // Refresh JWT token
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthRefreshResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Validate CSRF token
        if (!csrfService.isCsrfTokenValid(request)) {
            return ResponseEntity.status(403).build();
        }
        // Refresh the access token using the refresh token from the HttpOnly cookie
        AuthTokens tokens = authService.refreshToken(request);

        // Add the new refresh token to the HttpOnly cookie
        onlyCookieService.addRefreshTokenCookie(response, tokens.refreshToken());

        // Return the new access token in the response body
        return ResponseEntity.ok(new AuthRefreshResponse(tokens.accessToken()));
    }


    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        //Clear the HttpOnly cookie containing the refresh token
        onlyCookieService.clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }
}
