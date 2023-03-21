package app.taskmanagementsystem.domain.dto.view;

public class UserBasicViewDto {
    private Long id;
    private String username;
    private String email;
    private int age;
    private DepartmentAdminViewDto department;

    public UserBasicViewDto() {
    }

    public String getUsername() {
        return username;
    }

    public UserBasicViewDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserBasicViewDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserBasicViewDto setAge(int age) {
        this.age = age;
        return this;
    }

    public DepartmentAdminViewDto getDepartment() {
        return department;
    }

    public UserBasicViewDto setDepartment(DepartmentAdminViewDto department) {
        this.department = department;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserBasicViewDto setId(Long id) {
        this.id = id;
        return this;
    }
}
