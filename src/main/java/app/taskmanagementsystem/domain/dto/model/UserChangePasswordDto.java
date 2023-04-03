package app.taskmanagementsystem.domain.dto.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserChangePasswordDto {

    private String fullName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 2)
    private String oldPassword;
    @NotBlank
    @Size(min = 2)
    private String newPassword;
    @NotBlank
    @Size(min = 2)
    private String confirmNewPassword;

    public UserChangePasswordDto() {
    }

    public String getFullName() {
        return fullName;
    }

    public UserChangePasswordDto setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserChangePasswordDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public UserChangePasswordDto setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public UserChangePasswordDto setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public UserChangePasswordDto setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
        return this;
    }
}
