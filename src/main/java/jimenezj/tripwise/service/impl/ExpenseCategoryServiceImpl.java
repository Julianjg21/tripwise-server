package jimenezj.tripwise.service.impl;

import jimenezj.tripwise.dto.expense.ExpenseCategoryResponse;
import jimenezj.tripwise.exception.ResourceNotFoundException;
import jimenezj.tripwise.model.ExpenseCategory;
import jimenezj.tripwise.repository.ExpenseCategoryRepository;
import jimenezj.tripwise.service.ExpenseCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;

    // Injecting dependencies
    public ExpenseCategoryServiceImpl(ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    @Override
    public List<ExpenseCategoryResponse> getExpenseCategories() {
        List<ExpenseCategory> expenseExpenseCategoryList = expenseCategoryRepository.findAll(); // Get all expenses
                                                                                                // categories
        if (expenseExpenseCategoryList.isEmpty()) {
            throw new ResourceNotFoundException("No expense categories  were found.");
        }
        // return list of expense categories found in the database
        return expenseExpenseCategoryList.stream()
                .map(category -> new ExpenseCategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}
