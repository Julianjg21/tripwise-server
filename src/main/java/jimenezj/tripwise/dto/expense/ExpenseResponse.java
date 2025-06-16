package jimenezj.tripwise.dto.expense;


import jimenezj.tripwise.model.ExpenseCategory;

import java.time.LocalDate;

public record ExpenseResponse(
        long id,
        String description,
        double amount,
        ExpenseCategory expenseCategory,
        LocalDate date
) {
}

