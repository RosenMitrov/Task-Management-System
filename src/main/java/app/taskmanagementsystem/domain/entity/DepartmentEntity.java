package app.taskmanagementsystem.domain.entity;


import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "departments")
public class DepartmentEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "department_name", nullable = false, unique = true)
    private DepartmentTypeEnum departmentName;
    @Column(name = "department_description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "count_employees", nullable = false)
    private int count;
    @OneToMany(mappedBy = "department", targetEntity = UserEntity.class)
    private List<UserEntity> users;

    public DepartmentEntity() {
    }

    public DepartmentTypeEnum getDepartmentName() {
        return departmentName;
    }

    public DepartmentEntity setDepartmentName(DepartmentTypeEnum departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DepartmentEntity setDescription(String depInformation) {
        this.description = depInformation;
        return this;
    }

    public int getCount() {
        return count;
    }

    public DepartmentEntity setCount(int count) {
        this.count = count;
        return this;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public DepartmentEntity setUsers(List<UserEntity> employees) {
        this.users = employees;
        return this;
    }
}
