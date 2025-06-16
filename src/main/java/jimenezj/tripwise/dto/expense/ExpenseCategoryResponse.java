package jimenezj.tripwise.dto.expense;

import jimenezj.tripwise.enums.ExpenseCategoryEnum;

public record ExpenseCategoryResponse(
        Long id,
        ExpenseCategoryEnum name
) {
}
