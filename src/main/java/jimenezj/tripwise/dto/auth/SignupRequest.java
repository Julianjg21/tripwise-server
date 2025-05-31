package jimenezj.tripwise.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 3, max = 50) String  fullName,
        @NotBlank @Email String  email,
        @NotBlank @Size(min = 8, max = 100) String  password
) {
}
