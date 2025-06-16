package jimenezj.tripwise.repository;

import jimenezj.tripwise.enums.ExpenseCategoryEnum;
import jimenezj.tripwise.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    Optional<ExpenseCategory> findByName(ExpenseCategoryEnum name);
}
