package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.rest.RoleRestViewDto;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;

import java.util.List;

public interface UserRoleService {
    UserRoleEntity getUserRoleEntityByRoleType(RoleTypeEnum roleTypeEnum);

    void userRolesInitialization();

    List<UserRoleEntity> getAdminRoles();

    List<UserRoleEntity> getModeratorRoles();

    List<UserRoleEntity> getUserRoles();

    List<RoleRestViewDto> allRolesStats();



}
