package app.taskmanagementsystem.domain.dto.view;

import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class UserDetailsViewDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isEnabled;
    private int age;
    private LocalDateTime createdOn;
//    private DepartmentAdminViewDto department;
    private DepartmentTypeEnum department;
    private List<UserRoleViewDto> roles;
    private String showRoles;

    private List<TaskBasicViewDto> tasks;


    public UserDetailsViewDto() {

    }

    public Long getId() {
        return id;
    }

    public UserDetailsViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDetailsViewDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsViewDto setEmail(String email) {
        this.email = email;
        return this;
    }


    public String getFirstName() {
        return firstName;
    }

    public UserDetailsViewDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDetailsViewDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public UserDetailsViewDto setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserDetailsViewDto setAge(int age) {
        this.age = age;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public UserDetailsViewDto setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

//    public DepartmentAdminViewDto getDepartment() {
//        return department;
//    }
//
//    public UserDetailsViewDto setDepartment(DepartmentAdminViewDto department) {
//        this.department = department;
//        return this;
//    }


    public DepartmentTypeEnum getDepartment() {
        return department;
    }

    public UserDetailsViewDto setDepartment(DepartmentTypeEnum department) {
        this.department = department;
        return this;
    }

    public List<UserRoleViewDto> getRoles() {
        return roles;
    }

    public UserDetailsViewDto setRoles(List<UserRoleViewDto> roles) {
        this.roles = roles;
        return this;
    }

    public List<TaskBasicViewDto> getTasks() {
        return tasks;
    }

    public UserDetailsViewDto setTasks(List<TaskBasicViewDto> tasks) {
        this.tasks = tasks;
        return this;
    }

    public String getShowRoles() {
        return showRoles;
    }

    public UserDetailsViewDto setShowRoles(String showRoles) {
        this.showRoles = showRoles;
        return this;
    }
}
