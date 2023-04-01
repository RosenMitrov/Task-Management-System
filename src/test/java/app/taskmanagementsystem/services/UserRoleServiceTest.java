package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.view.rest.RoleRestViewDto;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.UserRoleRepository;
import app.taskmanagementsystem.services.impl.UserRoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {
    private UserRoleService userRoleServiceToTest;
    @Mock
    private UserRoleRepository mockUserRoleRepository;
    @Mock
    private ModelMapper mockModelMapper;

    @BeforeEach
    void setUp() {
        this.userRoleServiceToTest = new UserRoleServiceImpl(mockUserRoleRepository, mockModelMapper);
    }


    @Test
    void test_getUserRoleEntityByRoleType_ShouldThrow() {
        Arrays.stream(RoleTypeEnum.values())
                .forEach(roleTypeEnum -> {
                    Assertions.assertThrows(ObjNotFoundException.class, () -> this.userRoleServiceToTest
                            .getUserRoleEntityByRoleType(roleTypeEnum));
                });
    }

    @Test
    void test_getUserRoleEntityByRoleType_ShouldReturnItSuccessfully() {
        Arrays.stream(RoleTypeEnum.values())
                .forEach(roleTypeEnum -> {
                    UserRoleEntity expectedEntity = new UserRoleEntity()
                            .setRole(roleTypeEnum)
                            .setDescription("Description " + roleTypeEnum.name());
                    Mockito
                            .when(this.mockUserRoleRepository.findFirstByRole(roleTypeEnum))
                            .thenReturn(Optional.of(expectedEntity));
                    UserRoleEntity actualEntity = this.userRoleServiceToTest.getUserRoleEntityByRoleType(roleTypeEnum);

                    Assertions.assertEquals(expectedEntity, actualEntity);
                    Assertions.assertEquals(expectedEntity.getRole(), actualEntity.getRole());
                    Assertions.assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
                });
    }


    @Test
    void test_userRolesInitialization_ShouldPopulateDB_Successfully() {
        List<UserRoleEntity> expectedEntities = Arrays.stream(RoleTypeEnum.values())
                .map(roleTypeEnum -> new UserRoleEntity()
                        .setRole(roleTypeEnum)
                        .setDescription("Description " + roleTypeEnum.name())
                )
                .toList();

        Mockito
                .when(mockUserRoleRepository.saveAllAndFlush(Mockito.any()))
                .thenReturn(expectedEntities);

        this.userRoleServiceToTest.userRolesInitialization();

        List<UserRoleEntity> actualEntities = mockUserRoleRepository
                .saveAllAndFlush(Mockito.any());

        Assertions.assertEquals(expectedEntities, actualEntities);
        for (int index = 0; index < expectedEntities.size(); index++) {
            Assertions.assertEquals(expectedEntities.get(index), actualEntities.get(index));
            Assertions.assertEquals(expectedEntities.get(index).getRole(), actualEntities.get(index).getRole());
            Assertions.assertEquals(expectedEntities.get(index).getDescription(), actualEntities.get(index).getDescription());
        }
    }


    @Test
    void test_isDbInit_ShouldReturnTrue() {
        Mockito.when(mockUserRoleRepository.count())
                .thenReturn(50L);

        userRoleServiceToTest.userRolesInitialization();

        Mockito
                .verify(mockUserRoleRepository, Mockito.times(0))
                .saveAllAndFlush(Mockito.any());
    }


    @Test
    void test_isDbInit_ShouldReturnFalse() {
        Mockito.when(mockUserRoleRepository.count())
                .thenReturn(0L);
        userRoleServiceToTest.userRolesInitialization();
        Mockito
                .verify(mockUserRoleRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());
    }

    @Test
    void test_getAdminRoles_ShouldReturnThemSuccessfully() {
        List<UserRoleEntity> expectedRoles = List.of(
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.ADMIN),
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.MODERATOR),
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.USER)
        );

        Mockito
                .when(this.mockUserRoleRepository.findFirstByRole(RoleTypeEnum.ADMIN))
                .thenReturn(Optional.of(expectedRoles.get(0)));
        Mockito
                .when(this.mockUserRoleRepository.findFirstByRole(RoleTypeEnum.MODERATOR))
                .thenReturn(Optional.of(expectedRoles.get(1)));
        Mockito
                .when(this.mockUserRoleRepository.findFirstByRole(RoleTypeEnum.USER))
                .thenReturn(Optional.of(expectedRoles.get(2)));

        List<UserRoleEntity> actualRoles = this.userRoleServiceToTest.getAdminRoles();

        Assertions.assertEquals(expectedRoles, actualRoles);
        Assertions.assertEquals(expectedRoles.get(0), actualRoles.get(0));
        Assertions.assertEquals(expectedRoles.get(0).getRole(), actualRoles.get(0).getRole());

        Assertions.assertEquals(expectedRoles.get(1), actualRoles.get(1));
        Assertions.assertEquals(expectedRoles.get(1).getRole(), actualRoles.get(1).getRole());

        Assertions.assertEquals(expectedRoles.get(2), actualRoles.get(2));
        Assertions.assertEquals(expectedRoles.get(2).getRole(), actualRoles.get(2).getRole());
    }

    @Test
    void test_getModeratorRoles_ShouldReturnThemSuccessfully() {
        List<UserRoleEntity> expectedRoles = List.of(
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.MODERATOR),
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.USER)
        );

        Mockito
                .when(this.mockUserRoleRepository.findFirstByRole(RoleTypeEnum.MODERATOR))
                .thenReturn(Optional.of(expectedRoles.get(0)));
        Mockito
                .when(this.mockUserRoleRepository.findFirstByRole(RoleTypeEnum.USER))
                .thenReturn(Optional.of(expectedRoles.get(1)));

        List<UserRoleEntity> actualRoles = this.userRoleServiceToTest.getModeratorRoles();

        Assertions.assertEquals(expectedRoles, actualRoles);
        Assertions.assertEquals(expectedRoles.get(0), actualRoles.get(0));
        Assertions.assertEquals(expectedRoles.get(0).getRole(), actualRoles.get(0).getRole());

        Assertions.assertEquals(expectedRoles.get(1), actualRoles.get(1));
        Assertions.assertEquals(expectedRoles.get(1).getRole(), actualRoles.get(1).getRole());
    }

    @Test
    void test_getUserRoles_ShouldReturnThemSuccessfully() {
        List<UserRoleEntity> expectedRoles = List.of(
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.USER)
        );


        Mockito
                .when(this.mockUserRoleRepository.findFirstByRole(RoleTypeEnum.USER))
                .thenReturn(Optional.of(expectedRoles.get(0)));

        List<UserRoleEntity> actualRoles = this.userRoleServiceToTest.getUserRoles();

        Assertions.assertEquals(expectedRoles, actualRoles);
        Assertions.assertEquals(expectedRoles.get(0), actualRoles.get(0));
        Assertions.assertEquals(expectedRoles.get(0).getRole(), actualRoles.get(0).getRole());
    }

    @Test
    void test_getRoleByType_ShouldThrow() {
        Assertions.assertThrows(ObjNotFoundException.class, () -> this.userRoleServiceToTest
                .getUserRoles());
        Assertions.assertThrows(ObjNotFoundException.class, () -> this.userRoleServiceToTest
                .getAdminRoles());
        Assertions.assertThrows(ObjNotFoundException.class, () -> this.userRoleServiceToTest
                .getModeratorRoles());
    }

    @Test
    void test_allRolesStats_ShouldReturnThem() {
        List<UserRoleEntity> expectedRoles = List.of(
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.ADMIN)
                        .setDescription("Description 1")
                        .setUsers(List.of(
                                new UserEntity()
                                        .setFirstName("test first name"),
                                new UserEntity()
                                        .setFirstName("test last name"))),
                new UserRoleEntity()
                        .setRole(RoleTypeEnum.MODERATOR)
                        .setDescription("Description 2")
                        .setUsers(List.of(
                                new UserEntity()
                                        .setFirstName("test first name 1"),
                                new UserEntity()
                                        .setFirstName("test last name 1"),
                                new UserEntity()
                                        .setFirstName("test first name"))),

                new UserRoleEntity()
                        .setRole(RoleTypeEnum.USER)
                        .setDescription("Description 3")
                        .setUsers(List.of(
                                new UserEntity()
                                        .setFirstName("test first name 2"),
                                new UserEntity()
                                        .setFirstName("test last name 2"),
                                new UserEntity()
                                        .setFirstName("test first name"),
                                new UserEntity()
                                        .setFirstName("test last name 1")))
        );

        Mockito
                .when(mockUserRoleRepository.findAll())
                .thenReturn(expectedRoles);

        Mockito
                .when(mockModelMapper.map(expectedRoles.get(0), RoleRestViewDto.class))
                .thenReturn(new RoleRestViewDto()
                        .setRole(expectedRoles.get(0).getRole()).setCountUsers(1L));
        Mockito
                .when(mockModelMapper.map(expectedRoles.get(1), RoleRestViewDto.class))
                .thenReturn(new RoleRestViewDto()
                        .setRole(expectedRoles.get(1).getRole()).setCountUsers(1L));
        Mockito
                .when(mockModelMapper.map(expectedRoles.get(2), RoleRestViewDto.class))
                .thenReturn(new RoleRestViewDto()
                        .setRole(expectedRoles.get(2).getRole()).setCountUsers(1L));

        List<RoleRestViewDto> actualRoles = this.userRoleServiceToTest.allRolesStats();

        Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
        Assertions.assertEquals(expectedRoles.get(0).getRole(), actualRoles.get(0).getRole());
        Assertions.assertEquals(expectedRoles.get(0).getUsers().size(), actualRoles.get(0).getCountUsers());

        Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
        Assertions.assertEquals(expectedRoles.get(1).getRole(), actualRoles.get(1).getRole());
        Assertions.assertEquals(expectedRoles.get(1).getUsers().size(), actualRoles.get(1).getCountUsers());

        Assertions.assertEquals(expectedRoles.size(), actualRoles.size());
        Assertions.assertEquals(expectedRoles.get(2).getRole(), actualRoles.get(2).getRole());
        Assertions.assertEquals(expectedRoles.get(2).getUsers().size(), actualRoles.get(2).getCountUsers());
    }
}
