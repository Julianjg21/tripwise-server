package jimenezj.tripwise.controller;

import jakarta.validation.Valid;
import jimenezj.tripwise.dto.reset_password.CreatePasswordResetRequest;
import jimenezj.tripwise.dto.reset_password.PasswordResetRequest;
import jimenezj.tripwise.service.impl.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // Injecting dependencies
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // Endpoint to request a password reset
    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody @Valid CreatePasswordResetRequest request) {
        passwordResetService.createResetPassword(request);
        return ResponseEntity.ok("If the mail exists, the recovery email has been sent.");
    }

    // Endpoint to reset the password
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok("Updated password.");
    }
}
