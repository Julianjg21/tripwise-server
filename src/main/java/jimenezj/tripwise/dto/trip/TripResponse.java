package jimenezj.tripwise.dto.trip;


import java.time.LocalDate;

public record TripResponse(
        Long id,
        String name,
        String destiny,
        LocalDate startDate,
        LocalDate endDate) {
}
