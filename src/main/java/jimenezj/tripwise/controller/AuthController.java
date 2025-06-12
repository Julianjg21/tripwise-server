package jimenezj.tripwise.controller;
import jimenezj.tripwise.dto.auth.*;
import jimenezj.tripwise.dto.user.UserProfileResponse;
import jimenezj.tripwise.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Injecting the auth service
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Signup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("Successfully registered user.");
    }

    // Get info of the currently authenticated user
    @GetMapping("/userInfo")
    public ResponseEntity<UserProfileResponse> getAuthenticatedUser() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    // Refresh JWT token
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthRefreshResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
