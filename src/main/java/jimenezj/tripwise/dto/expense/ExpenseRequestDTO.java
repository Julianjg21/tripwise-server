package jimenezj.tripwise.dto.expense;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ExpenseRequestDTO(
        @NotBlank @Size(min = 3, max = 500) String description,
        @Min(0) double amount, 
        @Min(1) int categoryId, 
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date // Asegura formato YYYY-MM-DD
) {}

