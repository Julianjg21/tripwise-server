package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.exception.BadRequestException;

// Interface that defines the main auth methods
public interface AuthService {
    // User login, returns access + refresh tokens
    AuthResponse login(LoginRequest request);

    // Register new user
    void signup(SignupRequest request) throws BadRequestException;

    // Generate new access token using refresh token
    AuthRefreshResponse refreshToken(RefreshTokenRequest request);

}
