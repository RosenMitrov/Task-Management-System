package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.entity.ClassificationEntity;
import app.taskmanagementsystem.domain.entity.enums.ClassificationTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.ClassificationRepository;
import app.taskmanagementsystem.services.impl.ClassificationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClassificationServiceTest {

    private ClassificationService classificationServiceToTest;
    @Mock
    private ClassificationRepository mockClassificationRepository;

    @BeforeEach
    void setUp() {
        this.classificationServiceToTest = new ClassificationServiceImpl(mockClassificationRepository);
    }


    @Test
    void test_getClassificationEntityById_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> {
            long idNotFound = 55L;
            this.classificationServiceToTest.getClassificationEntityById(idNotFound);
        });
    }

    @Test
    void test_getClassificationEntityById_ShouldReturnItSuccessfully() {
        long classificationId = 55L;


        Arrays
                .stream(ClassificationTypeEnum.values())
                .forEach(type -> {
                    ClassificationEntity expectedClassification = new ClassificationEntity()
                            .setClassification(type);

                    Mockito.when(this.mockClassificationRepository.findById(classificationId))
                            .thenReturn(Optional.of(expectedClassification));

                    ClassificationEntity actualClassification = this.classificationServiceToTest
                            .getClassificationEntityById(classificationId);

                    Assertions.assertEquals(expectedClassification, actualClassification);
                });
    }

    @Test
    void test_getClassificationByEnumType_ShouldThrow() {
        Arrays.stream(ClassificationTypeEnum.values())
                .forEach(classificationTypeEnum -> {
                    Assertions
                            .assertThrows(ObjNotFoundException.class, () -> this.classificationServiceToTest
                                    .getClassificationByEnumType(classificationTypeEnum));
                });
    }

    @Test
    void test_getClassificationByEnumType_ShouldReturnItSuccessfully() {
        Arrays.stream(ClassificationTypeEnum.values())
                .forEach(typeEnum -> {

                    ClassificationEntity expectedEntity = (ClassificationEntity) new ClassificationEntity()
                            .setClassification(typeEnum)
                            .setId(1L);

                    Mockito.when(this.mockClassificationRepository
                                    .findFirstByClassification(typeEnum))
                            .thenReturn(Optional.of(expectedEntity));

                    ClassificationEntity actualEntity = this.classificationServiceToTest
                            .getClassificationByEnumType(typeEnum);

                    Assertions.assertEquals(expectedEntity, actualEntity);
                    Assertions.assertEquals(expectedEntity.getClassification(), actualEntity.getClassification());
                    Assertions.assertEquals(expectedEntity.getId(), actualEntity.getId());
                });
    }

    @Test
    void test_classificationInitialization_ShouldPopulateDB_Successfully() {
        List<ClassificationEntity> expectedEntities = Arrays.stream(ClassificationTypeEnum.values())
                .map(classificationTypeEnum -> new ClassificationEntity()
                        .setClassification(classificationTypeEnum))
                .toList();

        Mockito
                .when(mockClassificationRepository.saveAllAndFlush(Mockito.any()))
                .thenReturn(expectedEntities);

        this.classificationServiceToTest.classificationInitialization();

        Mockito
                .verify(mockClassificationRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());

        List<ClassificationEntity> actualEntities = mockClassificationRepository
                .saveAllAndFlush(Mockito.any());

        Assertions.assertEquals(expectedEntities, actualEntities);


        for (int index = 0; index < expectedEntities.size(); index++) {
            Assertions.assertEquals(expectedEntities.get(index), actualEntities.get(index));
            Assertions.assertEquals(expectedEntities.get(index).getClassification(), actualEntities.get(index).getClassification());
        }
    }

    @Test
    void test_isDbInit_ShouldReturnTrue() {
        Mockito.when(mockClassificationRepository.count())
                .thenReturn(50L);

        classificationServiceToTest.classificationInitialization();

        Mockito
                .verify(mockClassificationRepository, Mockito.times(0))
                .saveAllAndFlush(Mockito.any());
    }


    @Test
    void test_isDbInit_ShouldReturnFalse() {
        Mockito.when(mockClassificationRepository.count())
                .thenReturn(0L);
        classificationServiceToTest.classificationInitialization();
        Mockito
                .verify(mockClassificationRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());
    }

    @Test
    void test_getCount_ShouldReturnItSuccessfully(){
        long expectedCount = 50L;
        Mockito.when(mockClassificationRepository.count())
                .thenReturn(expectedCount);

        long actualCount = this.classificationServiceToTest.getCount();

        Assertions.assertEquals(expectedCount,actualCount);
    }
}
