package jimenezj.tripwise.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(
        @NotBlank(message = "Token is required")
        @Size(min = 20, max = 500, message = "Token must be between 20 and 500 characters")
        String token
) {
}
