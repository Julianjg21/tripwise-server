package jimenezj.tripwise.dto.expense;


public record ExpenseResponseDTO(
        int id,
        String tripName,
        String destiny,
        String startDate,
        String endDate
) {
}
