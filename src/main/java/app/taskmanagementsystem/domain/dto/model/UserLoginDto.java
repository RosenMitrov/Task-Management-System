package app.taskmanagementsystem.domain.dto.model;

public class UserLoginDto {

    private String email;


    public UserLoginDto() {
    }

    public String getEmail() {
        return email;
    }

    public UserLoginDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
