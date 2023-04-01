package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.entity.ProgressEntity;
import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.ProgressRepository;
import app.taskmanagementsystem.services.impl.ProgressServiceImpl;
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
class ProgressServiceTest {

    private ProgressService progressServiceToTest;

    @Mock
    private ProgressRepository mockProgressRepository;

    @BeforeEach
    void setUp() {
        this.progressServiceToTest = new ProgressServiceImpl(mockProgressRepository);
    }


    @Test
    void test_progressInitialization_ShouldPopulateDb_Successfully() {
        List<ProgressEntity> expectedEntities = Arrays.stream(ProgressTypeEnum.values())
                .map(progressTypeEnum -> new ProgressEntity()
                        .setProgress(progressTypeEnum))
                .toList();

        Mockito
                .when(mockProgressRepository.saveAllAndFlush(Mockito.any()))
                .thenReturn(expectedEntities);

        this.progressServiceToTest.progressInitialization();

        Mockito
                .verify(mockProgressRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());

        List<ProgressEntity> actualEntities = this.mockProgressRepository
                .saveAllAndFlush(Mockito.any());

        Assertions.assertEquals(expectedEntities, actualEntities);

        for (int index = 0; index < expectedEntities.size(); index++) {
            Assertions.assertEquals(expectedEntities.get(index), actualEntities.get(index));
            Assertions.assertEquals(expectedEntities.get(index).getProgress(), actualEntities.get(index).getProgress());
        }
    }


    @Test
    void test_getProgressEntityByType_ShouldThrow() {
        Arrays.stream(ProgressTypeEnum.values())
                .forEach(progressTypeEnum -> Assertions
                        .assertThrows(ObjNotFoundException.class,
                                () -> this.progressServiceToTest
                                        .getProgressEntityByType(progressTypeEnum)));
    }

    @Test
    void test_getProgressEntityByType_ShouldReturnItSuccessfully() {
        Arrays.stream(ProgressTypeEnum.values())
                .forEach(progressType -> {
                    ProgressEntity expectedEntity = new ProgressEntity().setProgress(progressType);
                    Mockito
                            .when(mockProgressRepository.findFirstByProgress(progressType))
                            .thenReturn(Optional.of(expectedEntity));

                    ProgressEntity actualEntity = this.progressServiceToTest
                            .getProgressEntityByType(progressType);

                    Assertions.assertEquals(expectedEntity, actualEntity);
                    Assertions.assertEquals(expectedEntity.getProgress(), actualEntity.getProgress());
                });
    }


    @Test
    void test_isDbInit_ShouldReturnTrue() {
        Mockito.when(mockProgressRepository.count())
                .thenReturn(50L);

        progressServiceToTest.progressInitialization();

        Mockito
                .verify(mockProgressRepository, Mockito.times(0))
                .saveAllAndFlush(Mockito.any());
    }


    @Test
    void test_isDbInit_ShouldReturnFalse() {
        Mockito.when(mockProgressRepository.count())
                .thenReturn(0L);
        progressServiceToTest.progressInitialization();
        Mockito
                .verify(mockProgressRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());
    }
}
