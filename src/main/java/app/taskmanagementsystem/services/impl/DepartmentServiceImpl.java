package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.view.DepartmentViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.DepartmentRepository;
import app.taskmanagementsystem.services.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService, DbInit {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean isDbInit() {
        return this.departmentRepository.count() != 0;
    }

    @Override
    public void departmentsInitialization() {
        if (isDbInit()) {
            return;
        }
        List<DepartmentEntity> allDepartments = Arrays
                .stream(
                        DepartmentTypeEnum.values()
                )
                .map(this::fromTypeNumToDepartmentEntity)
                .toList();

        this.departmentRepository.saveAllAndFlush(allDepartments);
    }

    @Override
    public DepartmentEntity getDepartmentEntityByTypeEnum(DepartmentTypeEnum department) {
        return this.departmentRepository.findFirstByDepartmentName(department);
    }

    @Override
    public void incrementUsersCountInDepartment(DepartmentEntity department) {
        int currentCount = department.getCount();
        currentCount++;
        updateDepartmentCountInDB(department, currentCount);
    }

    @Override
    public void decrementUsersCountInDepartment(DepartmentEntity department) {
        int currentCount = department.getCount();
        currentCount--;
        updateDepartmentCountInDB(department, currentCount);
    }

    @Override
    @Transactional
    public List<DepartmentViewDto> findAllDepartmentViews() {
        return this.departmentRepository
                .findAll()
                .stream()
                .map(this::fromDepartmentEntityToDepartmentViewDto)
                .toList();
    }

    @Override
    @Transactional
    public DepartmentViewDto getDepartmentDetailsViewByDepartmentId(Long departmentId) {
        Optional<DepartmentEntity> optionalDepartment = this.departmentRepository.findById(departmentId);
        if (optionalDepartment.isEmpty()) {
            throw new ObjNotFoundException();
        }
        return fromDepartmentEntityToDepartmentViewDto(
                optionalDepartment.get()
        );
    }

    private DepartmentViewDto fromDepartmentEntityToDepartmentViewDto(DepartmentEntity department) {
        return this.modelMapper.map(department, DepartmentViewDto.class);
    }

    private DepartmentEntity fromTypeNumToDepartmentEntity(DepartmentTypeEnum department) {
        return new DepartmentEntity()
                .setDepartmentName(department)
                .setDescription("Some basic information about " + department.name())
                .setCount(0);
    }

    private void updateDepartmentCountInDB(DepartmentEntity department,
                                           int newCountToBeSet) {
        department.setCount(newCountToBeSet);
        this.departmentRepository.saveAndFlush(department);
    }
}
