package app.taskmanagementsystem.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

    private String fullName;
    private String nickname;

    public AppUserDetails(String username,
                          String password,
                          Collection<? extends GrantedAuthority> authorities,
                          String fullName,
                          String nickname) {
        super(username, password, authorities);
        this.fullName = fullName;
        this.nickname = nickname;
    }

    public String getFullName() {
        return fullName;
    }

    public AppUserDetails setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public AppUserDetails setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
