package jimenezj.tripwise.service;

import jimenezj.tripwise.dto.expense.ExpenseRequest;
import jimenezj.tripwise.dto.expense.ExpenseResponse;
import java.util.List;

public interface ExpenseService {

    // Get all expenses by trip
    List<ExpenseResponse> getExpensesByTrip(Long tripId);

    // Create new expense for a trip
    ExpenseResponse createExpense(Long tripId, ExpenseRequest request);

    // Update a expense of a trip
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    // Delete a expense of a trip
    void deleteExpense(Long id);
}
