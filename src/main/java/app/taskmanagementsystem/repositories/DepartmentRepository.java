package app.taskmanagementsystem.repositories;

import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    DepartmentEntity findFirstByDepartmentName(DepartmentTypeEnum departmentName);
}
