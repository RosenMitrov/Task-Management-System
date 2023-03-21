package app.taskmanagementsystem.domain.dto.view.rest;

import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;

public class RoleRestViewDto {
    private Long id;
    private RoleTypeEnum role;
    private long countUsers;

    public RoleRestViewDto() {
    }

    public Long getId() {
        return id;
    }

    public RoleRestViewDto setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleTypeEnum getRole() {
        return role;
    }

    public RoleRestViewDto setRole(RoleTypeEnum role) {
        this.role = role;
        return this;
    }

    public long getCountUsers() {
        return countUsers;
    }

    public RoleRestViewDto setCountUsers(long countUsers) {
        this.countUsers = countUsers;
        return this;
    }
}
