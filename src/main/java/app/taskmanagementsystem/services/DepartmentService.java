package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.DepartmentViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;

import java.util.List;

public interface DepartmentService {

    void departmentsInitialization();

    DepartmentEntity getDepartmentEntityByTypeEnum(DepartmentTypeEnum department);

    void incrementUsersCountInDepartment(DepartmentEntity backEndDepartment);

    void decrementUsersCountInDepartment(DepartmentEntity department);

    List<DepartmentViewDto> findAllDepartmentViews();

    DepartmentViewDto getDepartmentDetailsViewByDepartmentId(Long departmentId);
}
