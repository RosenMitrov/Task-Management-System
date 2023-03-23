package app.taskmanagementsystem.domain.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "isEnabled", nullable = false)
    private boolean isEnabled;
    @Column(name = "age")
    private int age;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRoleEntity> roles;
    @ManyToMany(mappedBy = "assignedUsers")
    private List<TaskEntity> tasks;
    @Column(name = "last_pass_change_date")
    private LocalDateTime lastPasswordChangeDate;

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public UserEntity setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserEntity setAge(int age) {
        this.age = age;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public UserEntity setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public UserEntity setDepartment(DepartmentEntity department) {
        this.department = department;
        return this;
    }

    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public UserEntity setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
        return this;
    }

    public LocalDateTime getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public UserEntity setLastPasswordChangeDate(LocalDateTime lastPasswordChangeDate) {
        this.lastPasswordChangeDate = lastPasswordChangeDate;
        return this;
    }

    public void changeRoles(List<UserRoleEntity> newRoles){
        this.roles = new ArrayList<>();
        this.roles.addAll(newRoles);
    }

    public void removeUserFromAllTasks(UserEntity userEntity) {
        if (userEntity == null) {
            return;
        }
        this.tasks
                .forEach(taskEntity -> taskEntity
                        .removeUserFromAssignUsersList(userEntity)
                );
    }

    public void addTask(TaskEntity taskEntityById) {
        this.tasks.add(taskEntityById);
    }
}
