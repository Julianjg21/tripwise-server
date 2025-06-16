package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.expense.ExpenseCategoryResponse;

import java.util.List;

public interface ExpenseCategoryService {
    //Get expense categories
    List<ExpenseCategoryResponse> getExpenseCategories();
}
