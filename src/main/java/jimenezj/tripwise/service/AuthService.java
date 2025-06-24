package jimenezj.tripwise.service;

import jakarta.servlet.http.HttpServletRequest;
import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.exception.BadRequestException;

// Interface that defines the main auth methods
public interface AuthService {
    // User login, returns access + refresh tokens
    AuthTokens login(LoginRequest request);

    // Register new user
    void signup(SignupRequest request) throws BadRequestException;

    // Generate new access token using refresh token
    AuthTokens refreshToken(HttpServletRequest request);

}
