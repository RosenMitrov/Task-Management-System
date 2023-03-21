package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.DepartmentAdminViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;

import java.util.List;

public interface DepartmentService {

    void departmentsInitialization();

    DepartmentEntity getDepartmentEntityByTypeEnum(DepartmentTypeEnum department);

    void incrementDepartmentCount(DepartmentEntity backEndDepartment);

    void decrementDepartmentCount(DepartmentEntity department);

    List<DepartmentAdminViewDto> findAllDepartmentsAdminViews();

    DepartmentAdminViewDto getDepartmentAdminDetailsViewDtoById(Long departmentId);
}
