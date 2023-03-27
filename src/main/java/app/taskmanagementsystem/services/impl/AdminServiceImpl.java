package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.view.DepartmentAdminViewDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.services.AdminService;
import app.taskmanagementsystem.services.DepartmentService;
import app.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final DepartmentService departmentService;

    @Autowired
    public AdminServiceImpl(UserService userService,
                            DepartmentService departmentService) {
        this.userService = userService;
        this.departmentService = departmentService;
    }

    @Override
    public List<UserBasicViewDto> findAllUserAdminBasicViewsDto() {
        return this.userService.findAllBasicViewUsers();
    }

    @Override
    public UserDetailsViewDto getUserAdminDetailsViewDto(Long userId) {
        UserDetailsViewDto userById = this.userService.getUserDetailsViewByUserId(userId);
        String roles = userById
                .getRoles()
                .stream()
                .map(userRoleViewAdminDto -> userRoleViewAdminDto.getRole().name())
                .collect(Collectors.joining(", "));

        userById.setShowRoles(roles);
        return userById;
    }

    @Override
    public void deleteUserEntityById(Long userId) {
        this.userService.deleteUserEntityById(userId);
    }

    @Override
    public List<DepartmentAdminViewDto> findAllDepartmentsAdminViews() {
        return this.departmentService.findAllDepartmentsAdminViews();
    }

    @Override
    public DepartmentAdminViewDto getDepartmentAdminDetailsViewDtoById(Long departmentId) {
      return   this.departmentService.getDepartmentAdminDetailsViewDtoById(departmentId);
    }

    @Override
    public void updateUser(Long userId,
                           UserDetailsViewDto userDetailsViewDto) {
        this.userService.updateUser(userId, userDetailsViewDto);
    }

    @Override
    public void updateUserRole(Long userId,
                               RoleTypeEnum role) {
        this.userService.updateUserRole(userId, role);
    }

}
