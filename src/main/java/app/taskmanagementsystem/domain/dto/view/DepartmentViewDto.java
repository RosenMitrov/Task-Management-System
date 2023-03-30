package app.taskmanagementsystem.domain.dto.view;

import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;

import java.util.List;

public class DepartmentViewDto {

    private Long id;
    private DepartmentTypeEnum departmentName;
    private String description;
    private int count;
    private List<UserBasicViewDto> users;

    public DepartmentViewDto() {
    }

    public Long getId() {
        return id;
    }

    public DepartmentViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public DepartmentTypeEnum getDepartmentName() {
        return departmentName;
    }

    public DepartmentViewDto setDepartmentName(DepartmentTypeEnum departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DepartmentViewDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getCount() {
        return count;
    }

    public DepartmentViewDto setCount(int count) {
        this.count = count;
        return this;
    }

    public List<UserBasicViewDto> getUsers() {
        return users;
    }

    public DepartmentViewDto setUsers(List<UserBasicViewDto> users) {
        this.users = users;
        return this;
    }
}
