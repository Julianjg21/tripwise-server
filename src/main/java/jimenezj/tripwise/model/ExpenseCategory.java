package jimenezj.tripwise.model;

import jakarta.persistence.*;
import jimenezj.tripwise.enums.ExpenseCategoryEnum;

import java.util.List;

@Entity
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ExpenseCategoryEnum name;
    @OneToMany(mappedBy = "expenseCategory")
    private List<Expense> expenses;


    // Constructors
    public ExpenseCategory() {}
    public ExpenseCategory(ExpenseCategoryEnum name) {
        this.name = name;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public ExpenseCategoryEnum getName() {
        return name;
    }

    public void setName(ExpenseCategoryEnum name) {
        this.name = name;
    }
}
