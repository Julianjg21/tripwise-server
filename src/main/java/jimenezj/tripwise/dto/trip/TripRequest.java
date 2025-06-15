package jimenezj.tripwise.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TripRequest(
        @NotBlank @Size(min = 3, max = 50) String name,
        @NotBlank @Size(min = 3, max = 100) String destiny,
        LocalDate  startDate,
        LocalDate endDate
) {
}
