package app.taskmanagementsystem.domain.dto.view.rest;

public class UserBasicRestViewDto {

    private Long id;

    private String username;

    private int age;
    private String department;

    public UserBasicRestViewDto() {
    }

    public Long getId() {
        return id;
    }

    public UserBasicRestViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserBasicRestViewDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserBasicRestViewDto setAge(int age) {
        this.age = age;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public UserBasicRestViewDto setDepartment(String department) {
        this.department = department;
        return this;
    }
}
