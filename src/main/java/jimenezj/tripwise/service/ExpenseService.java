package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.expense.ExpenseRequest;
import jimenezj.tripwise.dto.expense.ExpenseResponse;
import org.springframework.data.domain.Page;


public interface ExpenseService {

    // Get all expenses by trip
    Page<ExpenseResponse> getExpensesByTrip(Long tripId, int page, int size);

    // Create new expense for a trip
    ExpenseResponse createExpense(Long tripId, ExpenseRequest request);

    // Update a expense of a trip
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    // Delete a expense of a trip
    void deleteExpense(Long id);
}
