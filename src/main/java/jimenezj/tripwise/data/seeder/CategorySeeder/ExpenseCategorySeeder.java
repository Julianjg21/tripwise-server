package jimenezj.tripwise.data.seeder.CategorySeeder;

import jakarta.annotation.PostConstruct;
import jimenezj.tripwise.enums.ExpenseCategoryEnum;
import jimenezj.tripwise.model.ExpenseCategory;
import jimenezj.tripwise.repository.ExpenseCategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class ExpenseCategorySeeder {
    private final ExpenseCategoryRepository    expenseCategoryRepository;

    // Injecting dependencies
    public ExpenseCategorySeeder(ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
    }


    @PostConstruct
    public void seedCategories() {
        // Iterate through all predefined categories in the ExpenseCategoryEnum
        for (ExpenseCategoryEnum categoryEnum : ExpenseCategoryEnum.values()) {

            // Check if the category already exists in the database
            boolean exists = expenseCategoryRepository.findByName(categoryEnum).isPresent();

            // If the category does not exist, save it
            if (!exists) {
                expenseCategoryRepository.save(new ExpenseCategory(categoryEnum));
                System.out.println("Saved category: " + categoryEnum.name());
            } else {
                System.out.println("Category already exists: " + categoryEnum.name());
            }
        }
    }

}
