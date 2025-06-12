package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.dto.user.UserProfileResponse;
import jimenezj.tripwise.exception.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;

// Interface that defines the main auth methods
public interface AuthService {
    // User login, returns access + refresh tokens
    AuthResponse login(LoginRequest request);

    // Register new user
    void signup(SignupRequest request) throws BadRequestException;

    // Generate new access token using refresh token
    AuthRefreshResponse refreshToken(RefreshTokenRequest request);

    // get the information of the authenticated user
    UserProfileResponse getAuthenticatedUser();
}
