package jimenezj.tripwise.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Expense {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String description;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    ExpenseCategory expenseCategory;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    Trip trip;

    // Constructors
    public Expense() {
    }

    public Expense(String description, double amount, ExpenseCategory expenseCategory, LocalDate date, Trip trip) {
        this.description = description;
        this.amount = amount;
        this.expenseCategory = expenseCategory;
        this.date = date;
        this.trip = trip;
    }

    // Getters and Setter
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public ExpenseCategory getCategory() { return expenseCategory;}
    public void setCategory(ExpenseCategory expenseCategory) {  this.expenseCategory = expenseCategory;}
    public String getDescription() {    return description;}
    public void setDescription(String description) {this.description = description;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) { this.amount = amount;}
    public LocalDate getDate() { return date;}
    public void setDate(LocalDate date) { this.date = date;}
    public Trip getTrip() {return trip;}
    public void setTrip(Trip trip) {this.trip = trip;}

}
