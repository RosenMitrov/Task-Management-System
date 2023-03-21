package app.taskmanagementsystem.domain.dto.view;

import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;

import java.util.List;

public class DepartmentAdminViewDto {

    private Long id;
    private DepartmentTypeEnum departmentName;
    private String description;
    private int count;
    private List<UserBasicViewDto> users;

    public DepartmentAdminViewDto() {
    }

    public Long getId() {
        return id;
    }

    public DepartmentAdminViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public DepartmentTypeEnum getDepartmentName() {
        return departmentName;
    }

    public DepartmentAdminViewDto setDepartmentName(DepartmentTypeEnum departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DepartmentAdminViewDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getCount() {
        return count;
    }

    public DepartmentAdminViewDto setCount(int count) {
        this.count = count;
        return this;
    }

    public List<UserBasicViewDto> getUsers() {
        return users;
    }

    public DepartmentAdminViewDto setUsers(List<UserBasicViewDto> users) {
        this.users = users;
        return this;
    }
}
