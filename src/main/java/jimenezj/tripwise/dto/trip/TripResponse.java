package jimenezj.tripwise.dto.trip;



public record TripResponse(
        int id,
        String tripName,
        String destiny,
        String startDate,
        String endDate) {
}
