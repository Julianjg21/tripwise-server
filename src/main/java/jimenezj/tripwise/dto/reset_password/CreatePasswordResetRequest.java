package jimenezj.tripwise.dto.reset_password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatePasswordResetRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email 
) {
}
