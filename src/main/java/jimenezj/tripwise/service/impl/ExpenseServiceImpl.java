package jimenezj.tripwise.service.impl;

import jimenezj.tripwise.dto.expense.ExpenseRequest;
import jimenezj.tripwise.dto.expense.ExpenseResponse;
import jimenezj.tripwise.exception.ResourceNotFoundException;
import jimenezj.tripwise.exception.UnauthorizedException;
import jimenezj.tripwise.model.ExpenseCategory;
import jimenezj.tripwise.model.Expense;
import jimenezj.tripwise.model.Trip;
import jimenezj.tripwise.repository.ExpenseCategoryRepository;
import jimenezj.tripwise.repository.ExpenseRepository;
import jimenezj.tripwise.repository.TripRepository;
import jimenezj.tripwise.security.impl.UserDetailsServiceImpl;
import jimenezj.tripwise.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

        private final ExpenseRepository expenseRepository;
        private final UserDetailsServiceImpl userDetailsServiceImpl;
        private final TripRepository tripRepository;
        private final ExpenseCategoryRepository expenseCategoryRepository;

        // Injecting dependencies
        public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserDetailsServiceImpl userDetailsServiceImpl,
                        TripRepository tripRepository, ExpenseCategoryRepository expenseCategoryRepository) {
                this.expenseRepository = expenseRepository;
                this.userDetailsServiceImpl = userDetailsServiceImpl;
                this.tripRepository = tripRepository;
                this.expenseCategoryRepository = expenseCategoryRepository;
        }

        // Get all expenses by trip id
        @Override
        public List<ExpenseResponse> getExpensesByTrip(Long tripId) {
                Long userId = userDetailsServiceImpl.getAuthenticatedUser().getId(); // get authenticated user

                // Check if trip exists and belongs to the authenticated user
                Trip trip = tripRepository.findById(tripId)
                                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

                if (!trip.getUser().getId().equals(userId)) {
                        throw new UnauthorizedException("You are not authorized to view expenses for this trip");
                }

                // Get all expenses by trip id
                return expenseRepository
                                .findAllByTripId(tripId).stream().map(expense -> new ExpenseResponse(expense.getId(),
                                                expense.getDescription(), expense.getAmount(), expense.getCategory(),
                                                expense.getDate()))
                                .collect(Collectors.toList());
        }

        // Create expense for a trip
        @Override
        public ExpenseResponse createExpense(Long tripId, ExpenseRequest request) {
                // Get expense categories
                ExpenseCategory expenseCategory = expenseCategoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                // Check if trip exists and belongs to the authenticated user
                Trip trip = tripRepository.findById(tripId)
                                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

                // Build expense
                Expense buildExpense = new Expense(request.description(), request.amount(), expenseCategory,
                                request.date(),
                                trip);

                Expense savedExpense = expenseRepository.save(buildExpense);

                return new ExpenseResponse(savedExpense.getId(), savedExpense.getDescription(),
                                savedExpense.getAmount(),
                                savedExpense.getCategory(), savedExpense.getDate());
        }

        // Update expense by expense id
        @Override
        public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
                Long userId = userDetailsServiceImpl.getAuthenticatedUser().getUser().getId(); // Get authenticated user
                // Get expense categories
                ExpenseCategory expenseCategory = expenseCategoryRepository.findById(request.categoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                // Get expense by expense id
                Expense expense = expenseRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

                if (expense.getTrip().getUser().getId() != userId) {
                        throw new UnauthorizedException("You do not have access to update  this expense.");
                }
                // Build expense with new data
                expense.setDescription(request.description());
                expense.setAmount(request.amount());
                expense.setCategory(expenseCategory);
                expense.setDate(request.date());

                Expense updateExpense = expenseRepository.save(expense);

                return new ExpenseResponse(updateExpense.getId(), updateExpense.getDescription(),
                                updateExpense.getAmount(),
                                updateExpense.getCategory(), updateExpense.getDate());
        }

        // Delete expense by expense id
        @Override
        public void deleteExpense(Long id) {
                Long userId = userDetailsServiceImpl.getAuthenticatedUser().getUser().getId(); // Get authenticated user
                Expense expense = expenseRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

                if (expense.getTrip().getUser().getId() != userId) {
                        throw new UnauthorizedException("You do not have access to delete this expense.");
                }

                expenseRepository.deleteById(id);
        }
}
