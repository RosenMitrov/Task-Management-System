package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.view.DepartmentViewDto;
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
    public List<UserBasicViewDto> findAllUserBasicViewsDto() {
        return this.userService.findAllUserBasicViewsDto();
    }

    @Override
    public UserDetailsViewDto getUserDetailsViewDtoByUserId(Long userId) {
        UserDetailsViewDto userDetailsViewDto = this.userService.getUserDetailsViewByUserId(userId);
        String roles = userDetailsViewDto
                .getRoles()
                .stream()
                .map(userRoleViewAdminDto -> userRoleViewAdminDto.getRole().name())
                .collect(Collectors.joining(", "));

        userDetailsViewDto.setShowRoles(roles);
        return userDetailsViewDto;
    }

    @Override
    public boolean deleteUserEntityByUserId(Long userId,
                                            String email) {
        return this.userService.deleteUserEntityByUserId(userId, email);
    }

    @Override
    public List<DepartmentViewDto> findAllDepartmentViews() {
        return this.departmentService.findAllDepartmentViews();
    }

    @Override
    public DepartmentViewDto getDepartmentDetailsViewByDepartmentId(Long departmentId) {
        return this.departmentService.getDepartmentDetailsViewByDepartmentId(departmentId);
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
