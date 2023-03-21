package app.taskmanagementsystem.domain.dto.view;

import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;

public class UserRoleViewDto {
    private RoleTypeEnum role;

    public UserRoleViewDto() {
    }

    public RoleTypeEnum getRole() {
        return role;
    }

    public UserRoleViewDto setRole(RoleTypeEnum role) {
        this.role = role;
        return this;
    }
}
