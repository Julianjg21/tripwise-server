package jimenezj.tripwise.dto.trip;



public record TripResponseDTO(  
        int id,
        String tripName,
        String destiny,
        String startDate,
        String endDate) {
}
