package jimenezj.tripwise.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
        String fullName
) {
}
