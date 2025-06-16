package jimenezj.tripwise.dto.expense;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ExpenseRequest(
        @NotBlank(message = "Description is required")
        @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
        String description,

        @Min(value = 0, message = "Amount must be a positive value")
        double amount,

        @NotNull(message = "Category ID is required")
        @Min(value = 1, message = "Category ID must be a valid positive number")
        Long categoryId,

        @NotNull(message = "Date is required")
        LocalDate date
) {}

