package jimenezj.tripwise.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Trip {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String name;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Contructors
    public Trip() {
    }

    public Trip(String name, String destination, LocalDate startDate, LocalDate endDate, User user) {
        this.endDate = endDate;
        this.user = user;
        this.startDate = startDate;
        this.destination = destination;
        this.name = name;
    }

    // Getters and Setters
    public User getUser() {return user;}
    public void setUser(User user) { this.user = user;}
    public long getId() {   return id;}
    public void setId(long id) { this.id = id;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name;}
    public String getDestination() { return destination;}
    public void setDestination(String destination) {this.destination = destination;}
    public LocalDate getStartDate() { return startDate;}
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() {return endDate;}
    public void setEndDate(LocalDate endDate) { this.endDate = endDate;}

}
