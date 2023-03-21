package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.DepartmentAdminViewDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;

import java.util.List;

public interface AdminService {
    List<UserBasicViewDto> findAllUserAdminBasicViewsDto();

    UserDetailsViewDto getUserAdminDetailsViewDto(Long userId);

    void deleteUserEntityById(Long userId);

    List<DepartmentAdminViewDto> findAllDepartmentsAdminViews();

    DepartmentAdminViewDto getDepartmentAdminDetailsViewDtoById(Long departmentId);

    void updateUser(Long userId,
                    UserDetailsViewDto userDetailsViewDto);

    void updateUserRole(Long userId,
                        RoleTypeEnum role);
}
