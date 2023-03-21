package app.taskmanagementsystem.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

    private String fullName;

    public AppUserDetails(String username,
                          String password,
                          Collection<? extends GrantedAuthority> authorities,
                          String fullName) {
        super(username, password, authorities);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public AppUserDetails setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
}
