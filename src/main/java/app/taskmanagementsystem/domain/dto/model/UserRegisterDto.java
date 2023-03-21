package app.taskmanagementsystem.domain.dto.model;

import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
public class UserRegisterDto {

    @NotBlank
    @Size(min = 2)
    private String firstName;
    @NotBlank
    @Size(min = 2)
    private String lastName;
    @NotBlank
    @Size(min = 2)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Min(18)
    private Integer age;
    @NotBlank
    @Size(min = 2)
    private String password;
    @NotBlank
    @Size(min = 2)
    private String confirmPassword;
    @NotNull
    private DepartmentTypeEnum department;

    private boolean isEnabled;
    private LocalDateTime createdOn;

    public UserRegisterDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRegisterDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserRegisterDto setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterDto setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public UserRegisterDto setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public UserRegisterDto setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public DepartmentTypeEnum getDepartment() {
        return department;
    }

    public UserRegisterDto setDepartment(DepartmentTypeEnum department) {
        this.department = department;
        return this;
    }
}
