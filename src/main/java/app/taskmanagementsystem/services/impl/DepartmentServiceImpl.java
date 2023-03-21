package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.view.DepartmentAdminViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
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
    public void incrementDepartmentCount(DepartmentEntity department) {
        int currentCount = department.getCount();
        currentCount++;
        department.setCount(currentCount);
        this.departmentRepository.saveAndFlush(department);
    }

    @Override
    public void decrementDepartmentCount(DepartmentEntity department) {
        int currentCount = department.getCount();
        currentCount--;
        department.setCount(currentCount);
        this.departmentRepository.saveAndFlush(department);
    }

    @Override
    @Transactional
    public List<DepartmentAdminViewDto> findAllDepartmentsAdminViews() {
        return this.departmentRepository
                .findAll()
                .stream()
                .map(this::fromEntityToDepartmentAdminViewDto)
                .toList();

    }

    @Override
    @Transactional
    public DepartmentAdminViewDto getDepartmentAdminDetailsViewDtoById(Long departmentId) {
        Optional<DepartmentEntity> optionalDepartmentById = this.departmentRepository.findById(departmentId);

        if (optionalDepartmentById.isEmpty()) {
            // TODO: 3/17/2023 think about exceptions
            return null;
        }
        return fromEntityToDepartmentAdminViewDto(
                optionalDepartmentById.get()
        );
    }

    private DepartmentAdminViewDto fromEntityToDepartmentAdminViewDto(DepartmentEntity department) {
        return this.modelMapper.map(department, DepartmentAdminViewDto.class);
    }

    private DepartmentEntity fromTypeNumToDepartmentEntity(DepartmentTypeEnum department) {
        return new DepartmentEntity()
                .setDepartmentName(department)
                .setDescription("Some basic information about " + department.name())
                .setCount(0);
    }
}
