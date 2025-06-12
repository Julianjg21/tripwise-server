package jimenezj.tripwise.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TripRequest(
        @NotBlank @Size(min = 3, max = 50) String tripName,
        @NotBlank @Size(min = 3, max = 100) String destiny,
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String startDate,
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String endDate
) {
}
