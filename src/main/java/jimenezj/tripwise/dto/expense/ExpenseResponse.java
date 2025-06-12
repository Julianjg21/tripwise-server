package jimenezj.tripwise.dto.expense;


public record ExpenseResponse(
        int id,
        String tripName,
        String destiny,
        String startDate,
        String endDate
) {
}
