package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.DepartmentViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.DepartmentRepository;
import app.taskmanagementsystem.services.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    private DepartmentService departmentServiceToTest;

    @Mock
    private DepartmentRepository mockDepartmentRepository;
    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        this.departmentServiceToTest = new DepartmentServiceImpl(mockDepartmentRepository, mockModelMapper);
    }


    @Test
    void test_departmentsInitialization_ShouldPopulateDB_Successfully() {
        List<DepartmentEntity> expectedEntities = Arrays
                .stream(
                        DepartmentTypeEnum.values()
                )
                .map(typeEnum -> new DepartmentEntity()
                        .setDepartmentName(typeEnum)
                        .setDescription("Description " + typeEnum.name())
                        .setCount(0))
                .toList();

        Mockito
                .when(mockDepartmentRepository.saveAllAndFlush(Mockito.any()))
                .thenReturn(expectedEntities);

        this.departmentServiceToTest.departmentsInitialization();

        Mockito
                .verify(mockDepartmentRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());

        List<DepartmentEntity> actualEntities = this.mockDepartmentRepository
                .saveAllAndFlush(Mockito.any());

        Assertions.assertEquals(expectedEntities, actualEntities);

        for (int index = 0; index < expectedEntities.size(); index++) {
            Assertions.assertEquals(expectedEntities.get(index), actualEntities.get(index));
            Assertions.assertEquals(expectedEntities.get(index).getCount(), actualEntities.get(index).getCount());
            Assertions.assertEquals(expectedEntities.get(index).getDepartmentName(), actualEntities.get(index).getDepartmentName());
            Assertions.assertEquals(expectedEntities.get(index).getDescription(), actualEntities.get(index).getDescription());
        }
    }


    @Test
    void test_getDepartmentEntityByTypeEnum_ShouldReturnItSuccessfully() {
        Arrays
                .stream(
                        DepartmentTypeEnum.values()
                ).forEach(typeEnum -> {
                    DepartmentEntity expectedEntity = new DepartmentEntity()
                            .setDepartmentName(typeEnum)
                            .setCount(1)
                            .setDescription("Description " + typeEnum.name());
                    Mockito
                            .when(this.mockDepartmentRepository
                                    .findFirstByDepartmentName(typeEnum))
                            .thenReturn(expectedEntity
                            );

                    DepartmentEntity actualEntity = this.departmentServiceToTest
                            .getDepartmentEntityByTypeEnum(typeEnum);


                    Assertions.assertEquals(expectedEntity, actualEntity);
                    Assertions.assertEquals(expectedEntity.getCount(), actualEntity.getCount());
                    Assertions.assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
                    Assertions.assertEquals(expectedEntity.getDepartmentName(), actualEntity.getDepartmentName());
                });
    }

    @Test
    void test_incrementUsersCountInDepartment_ShouldIncrementItSuccessfully() {
        DepartmentEntity department = new DepartmentEntity()
                .setCount(10);
        int expectedCount = 11;

        this.departmentServiceToTest.incrementUsersCountInDepartment(department);

        int actualCount = department.getCount();
        Assertions.assertEquals(expectedCount, actualCount);
    }

    @Test
    void test_decrementUsersCountInDepartment_ShouldIncrementItSuccessfully() {
        DepartmentEntity department = new DepartmentEntity()
                .setCount(10);
        int expectedCount = 9;

        this.departmentServiceToTest.decrementUsersCountInDepartment(department);

        int actualCount = department.getCount();
        Assertions.assertEquals(expectedCount, actualCount);
    }


    @Test
    void test_findAllDepartmentViews_ShouldReturnThemSuccessfully() {
        List<DepartmentEntity> expectedDepartments = List.of(
                new DepartmentEntity()
                        .setCount(10)
                        .setDescription("First Department")
                        .setDepartmentName(DepartmentTypeEnum.BACK_END_DEVELOPMENT_TEAM),
                new DepartmentEntity()
                        .setCount(3)
                        .setDescription("First Department")
                        .setDepartmentName(DepartmentTypeEnum.FRONT_END_DEVELOPMENT_TEAM)
        );

        Mockito
                .when(this.mockDepartmentRepository
                        .findAll())
                .thenReturn(expectedDepartments);
        Mockito
                .when(mockModelMapper.map(expectedDepartments.get(0), DepartmentViewDto.class))
                .thenReturn(new DepartmentViewDto()
                        .setCount(expectedDepartments.get(0).getCount())
                        .setDepartmentName(expectedDepartments.get(0).getDepartmentName())
                        .setDescription(expectedDepartments.get(0).getDescription()));

        Mockito
                .when(mockModelMapper.map(expectedDepartments.get(1), DepartmentViewDto.class))
                .thenReturn(new DepartmentViewDto()
                        .setCount(expectedDepartments.get(1).getCount())
                        .setDepartmentName(expectedDepartments.get(1).getDepartmentName())
                        .setDescription(expectedDepartments.get(1).getDescription()));

        List<DepartmentViewDto> actualDepartments = this.departmentServiceToTest.findAllDepartmentViews();


        Mockito.verify(mockModelMapper, Mockito.times(2))
                .map(Mockito.any(), ArgumentMatchers.eq(DepartmentViewDto.class));

        Assertions.assertEquals(expectedDepartments.size(), actualDepartments.size());

        Assertions.assertEquals(expectedDepartments.get(0).getDepartmentName(), actualDepartments.get(0).getDepartmentName());
        Assertions.assertEquals(expectedDepartments.get(0).getCount(), actualDepartments.get(0).getCount());
        Assertions.assertEquals(expectedDepartments.get(0).getDescription(), actualDepartments.get(0).getDescription());

        Assertions.assertEquals(expectedDepartments.get(1).getDepartmentName(), actualDepartments.get(1).getDepartmentName());
        Assertions.assertEquals(expectedDepartments.get(1).getCount(), actualDepartments.get(1).getCount());
        Assertions.assertEquals(expectedDepartments.get(1).getDescription(), actualDepartments.get(1).getDescription());
    }

    @Test
    void test_getDepartmentDetailsViewByDepartmentId_ShouldReturnItSuccessfully() {
        long departmentId = 42L;

        DepartmentEntity expectedDepartment = new DepartmentEntity()
                .setCount(10)
                .setDescription("First Department")
                .setDepartmentName(DepartmentTypeEnum.BACK_END_DEVELOPMENT_TEAM);
        Mockito.when(this.mockDepartmentRepository.findById(departmentId))
                .thenReturn(Optional.of(expectedDepartment));

        Mockito
                .when(mockModelMapper.map(expectedDepartment, DepartmentViewDto.class))
                .thenReturn(new DepartmentViewDto()
                        .setDescription(expectedDepartment.getDescription())
                        .setDepartmentName(expectedDepartment.getDepartmentName())
                        .setCount(expectedDepartment.getCount()));

        DepartmentViewDto actualDepartment = this.departmentServiceToTest.getDepartmentDetailsViewByDepartmentId(departmentId);

        Assertions.assertEquals(expectedDepartment.getCount(), actualDepartment.getCount());
        Assertions.assertEquals(expectedDepartment.getDescription(), actualDepartment.getDescription());
        Assertions.assertEquals(expectedDepartment.getDepartmentName(), actualDepartment.getDepartmentName());
    }

    @Test
    void test_getDepartmentDetailsViewByDepartmentId_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            long IdNotFound = 13L;
            this.departmentServiceToTest.getDepartmentDetailsViewByDepartmentId(IdNotFound);
        });
    }


    @Test
    void test_isDbInit_ShouldReturnTrue() {
        Mockito.when(mockDepartmentRepository.count())
                .thenReturn(50L);

        departmentServiceToTest.departmentsInitialization();

        Mockito
                .verify(mockDepartmentRepository, Mockito.times(0))
                .saveAllAndFlush(Mockito.any());
    }


    @Test
    void test_isDbInit_ShouldReturnFalse() {
        Mockito.when(mockDepartmentRepository.count())
                .thenReturn(0L);
        departmentServiceToTest.departmentsInitialization();
        Mockito
                .verify(mockDepartmentRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());
    }
}
