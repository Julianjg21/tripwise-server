package jimenezj.tripwise.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) //
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Automatically set to current time when created


    //Constructors
    public PasswordResetToken(User user, String token, LocalDateTime expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public PasswordResetToken() {
        // Default constructor for JPA
    }


    // Getters and Setters

    public UUID getId() { return id;}

    public void setId(UUID id) {this.id = id;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public LocalDateTime getExpiration() {  return expiration;}

    public void setExpiration(LocalDateTime expiration) {this.expiration = expiration;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt;}
}
