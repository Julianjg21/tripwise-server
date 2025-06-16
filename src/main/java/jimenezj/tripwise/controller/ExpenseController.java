package jimenezj.tripwise.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jimenezj.tripwise.dto.expense.ExpenseCategoryResponse;
import jimenezj.tripwise.dto.expense.ExpenseRequest;
import jimenezj.tripwise.dto.expense.ExpenseResponse;
import jimenezj.tripwise.service.ExpenseCategoryService;
import jimenezj.tripwise.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    // Injecting expense service and expense category service 
    private final ExpenseService expenseService;
    private final ExpenseCategoryService expenseCategoryService;
    public ExpenseController(ExpenseService expenseService, ExpenseCategoryService expenseCategoryService) {
        this.expenseService = expenseService;
        this.expenseCategoryService = expenseCategoryService;
    }
    
    //Get all expenses by trip
    @GetMapping("/trips/{tripId}/expenses")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByTrip(@PathVariable @Min(1) Long tripId) {
        List<ExpenseResponse> expenseResponseList = expenseService.getExpensesByTrip(tripId);
        return ResponseEntity.ok(expenseResponseList);
    }

    //Create expense for a trip
    @PostMapping("/trips/{tripId}/expenses")
    public ResponseEntity<ExpenseResponse> createExpense(@PathVariable @Min(1) Long tripId, @RequestBody @Valid ExpenseRequest request) {
        ExpenseResponse expenseResponse = expenseService.createExpense(tripId, request);
        return ResponseEntity.ok(expenseResponse);
    }

    //Update expense by expense id
    @PutMapping("/expenses/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable @Min(1) Long id, @RequestBody @Valid ExpenseRequest request) {
        ExpenseResponse expenseResponse = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(expenseResponse);
    }
    
    //delete expense by expense id
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable @Min(1) Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
    
    //Get all expense categories
    @GetMapping("expenses/categories")
    public ResponseEntity<List<ExpenseCategoryResponse>> getExpenseCategories() {
        List<ExpenseCategoryResponse> expenseResponseList = expenseCategoryService.getExpenseCategories();
       return ResponseEntity.ok(expenseResponseList);
        
    }
}
