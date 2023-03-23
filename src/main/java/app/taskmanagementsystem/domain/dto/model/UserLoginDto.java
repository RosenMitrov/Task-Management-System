package app.taskmanagementsystem.domain.dto.model;

public class UserLoginDto {

    private String email;
    private boolean rememberMe;

    public UserLoginDto() {
    }

    public String getEmail() {
        return email;
    }

    public UserLoginDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public UserLoginDto setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }
}
