package jimenezj.tripwise.security.impl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jimenezj.tripwise.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String fullName;

    // Constructor that maps our User model to Spring Security's UserDetails
    public UserDetailsImpl(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.fullName = user.getFullName();
        this.id = user.getId();
    }

    //We do not use roles at this time, but they are added for future implementations
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public String getFullName() {
        return fullName;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    // Spring uses this as the username (we use email)
    @Override
    public String getUsername() {
        return email;
    }

    // All these return true because we don't handle those states
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
