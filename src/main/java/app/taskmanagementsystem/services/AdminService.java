package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.DepartmentViewDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;

import java.util.List;

public interface AdminService {
    List<UserBasicViewDto> findAllUserBasicViewsDto();

    UserDetailsViewDto getUserDetailsViewDtoByUserId(Long userId);

    boolean deleteUserEntityByUserId(Long userId, String email);

    List<DepartmentViewDto> findAllDepartmentViews();

    DepartmentViewDto getDepartmentDetailsViewByDepartmentId(Long departmentId);

    void updateUser(Long userId,
                    UserDetailsViewDto userDetailsViewDto);

    void updateUserRole(Long userId,
                        RoleTypeEnum role);
}
