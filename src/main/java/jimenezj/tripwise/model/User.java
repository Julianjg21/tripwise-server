package jimenezj.tripwise.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String password;

    // Constructor
    public User() {
    }

    // Getters and Setters
    public String getFullName() {   return fullName;}
    public void setFullName(String fullName) { this.fullName = fullName; }
     public String getEmail() {  return email;  }
    public String getUsername() {return email;} // Returns the user's email, which is used as the username for authentication
    public void setEmail(String email) { this.email = email; }
    public String getPassword() {  return password;}
    public void setPassword(String password) { this.password = password;}
    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}
}
