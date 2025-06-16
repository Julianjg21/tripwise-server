package jimenezj.tripwise.dto.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TripRequest(

        @NotBlank(message = "Trip name is required")
        @Size(min = 3, max = 50, message = "Trip name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Destiny is required")
        @Size(min = 3, max = 100, message = "Destiny must be between 3 and 100 characters")
        String destiny,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate 
) {
}
