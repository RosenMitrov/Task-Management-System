package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.UserRegisterDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.dto.view.rest.UserBasicRestViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.TaskEntity;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userServiceToTest;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserRoleService mockUserRoleService;
    @Mock
    private DepartmentService mockDepartmentService;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private EmailService mockEmailService;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.userServiceToTest = new UserServiceImpl(mockUserRepository, mockUserRoleService, mockDepartmentService, mockModelMapper, mockPasswordEncoder, mockEmailService);
    }

    @Test
    void test_checkIfPasswordsMatch_ShouldReturnTrue() {
        UserRegisterDto userRegisterDto = new UserRegisterDto()
                .setPassword("topsecret")
                .setConfirmPassword("topsecret");
        boolean isPasswordsMatch = this.userServiceToTest.checkIfPasswordsMatch(userRegisterDto);

        Assertions
                .assertTrue(isPasswordsMatch);
    }

    @Test
    void test_checkIfPasswordsMatch_ShouldReturnFalse() {
        UserRegisterDto userRegisterDto = new UserRegisterDto()
                .setPassword("noMatching")
                .setConfirmPassword("topsecret");
        boolean isPasswordsMatch = this.userServiceToTest.checkIfPasswordsMatch(userRegisterDto);

        Assertions
                .assertFalse(isPasswordsMatch);
    }

    @Test
    void test_getUserEntityByEmail_ShouldThrow() {
        Assertions
                .assertThrows(UsernameNotFoundException.class, () -> {
                    String email = "not-found@email.bg";
                    this.userServiceToTest.getUserEntityByEmail(email);
                });
    }

    @Test
    void test_getUserEntityByEmail_ShouldReturnItSuccessfully() {
        String email = "email-exist@example.bg";
        UserEntity expected = new UserEntity()
                .setEmail(email)
                .setFirstName("Test")
                .setLastName("testov");

        Mockito
                .when(mockUserRepository.findFirstByEmail(email))
                .thenReturn(Optional.of(expected));

        UserEntity actualEntity = this.userServiceToTest.getUserEntityByEmail(email);

        Assertions.assertEquals(expected, actualEntity);
        Assertions.assertEquals(expected.hashCode(), actualEntity.hashCode());
        Assertions.assertEquals(expected.getEmail(), actualEntity.getEmail());
        Assertions.assertEquals(expected.getFirstName(), actualEntity.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actualEntity.getLastName());
    }

    @Test
    void test_findAllUserBasicViewsDto_ShouldReturnThemSuccessfully() {
        UserEntity firstEntity = new UserEntity()
                .setEmail("first-email@example.bg")
                .setAge(33);
        UserEntity secondEntity = new UserEntity()
                .setEmail("second-email@example.bg")
                .setAge(35);

        List<UserEntity> expectedUsers = List.of(
                firstEntity,
                secondEntity
        );
        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(expectedUsers);
        Mockito
                .when(mockModelMapper.map(firstEntity, UserBasicViewDto.class))
                .thenReturn(new UserBasicViewDto()
                        .setEmail(firstEntity.getEmail())
                        .setAge(firstEntity.getAge()));
        Mockito
                .when(mockModelMapper.map(secondEntity, UserBasicViewDto.class))
                .thenReturn(new UserBasicViewDto()
                        .setEmail(secondEntity.getEmail())
                        .setAge(secondEntity.getAge()));

        List<UserBasicViewDto> actualUserBasicViews = this.userServiceToTest.findAllUserBasicViewsDto();

        Assertions.assertEquals(expectedUsers.size(), actualUserBasicViews.size());
        Assertions.assertEquals(firstEntity.getEmail(), actualUserBasicViews.get(0).getEmail());
        Assertions.assertEquals(firstEntity.getAge(), actualUserBasicViews.get(0).getAge());
        Assertions.assertEquals(secondEntity.getEmail(), actualUserBasicViews.get(1).getEmail());
        Assertions.assertEquals(secondEntity.getAge(), actualUserBasicViews.get(1).getAge());
    }

    @Test
    void test_getUserDetailsViewByUserId_ShouldThrow() {
        Assertions
                .assertThrows(ObjNotFoundException.class, () -> {
                    long userId = 13L;
                    this.userServiceToTest.getUserDetailsViewByUserId(userId);
                });
    }


    @Test
    void test_getUserDetailsViewByUserId_ShouldReturnItSuccessfully() {
        long userId = 69L;

        UserEntity expectedUser = new UserEntity()
                .setEmail("email-exists@example.bg")
                .setUsername("test")
                .setAge(20);

        Mockito
                .when(this.mockUserRepository.findById(userId))
                .thenReturn(Optional.of(expectedUser));

        Mockito
                .when(mockModelMapper.map(expectedUser, UserDetailsViewDto.class))
                .thenReturn(new UserDetailsViewDto()
                        .setEmail(expectedUser.getEmail())
                        .setUsername(expectedUser.getUsername())
                        .setAge(expectedUser.getAge()));

        UserDetailsViewDto actualUser = this.userServiceToTest.getUserDetailsViewByUserId(userId);

        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(expectedUser.getAge(), actualUser.getAge());

    }

    @Test
    void test_deleteUserEntityByUserId_ShouldThrowAsTHeUserInSessionIsNotFound() {
        Assertions
                .assertThrows(ObjNotFoundException.class, () -> {
                    long idOfUserToBeRemoved = 13L;
                    String userInSessionEmail = "not-found@email.bg";
                    this.userServiceToTest.deleteUserEntityByUserId(idOfUserToBeRemoved, userInSessionEmail);
                });
    }


    @Test
    void test_deleteUserEntityByUserId_ShouldThrowAsTheUserWhoWIllBeRemovedIsNotFound() {
        Assertions
                .assertThrows(ObjNotFoundException.class, () -> {
                    long idOfUserToBeRemoved = 13L;
                    String userInSessionEmail = "emal-found@userInSessionEmail.bg";

                    UserEntity userInSession = new UserEntity()
                            .setEmail(userInSessionEmail);
                    Mockito
                            .when(this.mockUserRepository.findFirstByEmail(userInSessionEmail))
                            .thenReturn(Optional.of(userInSession));

                    this.userServiceToTest.deleteUserEntityByUserId(idOfUserToBeRemoved, userInSessionEmail);
                });
    }

    @Test
    void test_deleteUserEntityByUserId_ShouldReturnFalseAsIdsAreNotSame() {
        long idOfUserToBeRemoved = 69L;
        String userInSessionEmail = "emal-found@userInSessionEmail.bg";

        UserEntity userInSession = (UserEntity) new UserEntity()
                .setEmail(userInSessionEmail)
                .setId(idOfUserToBeRemoved);
        UserEntity userToBeRemoved = (UserEntity) new UserEntity()
                .setEmail(userInSessionEmail)
                .setId(idOfUserToBeRemoved);


        Mockito
                .when(this.mockUserRepository.findFirstByEmail(userInSessionEmail))
                .thenReturn(Optional.of(userInSession));
        Mockito
                .when(this.mockUserRepository.findById(idOfUserToBeRemoved))
                .thenReturn(Optional.of(userToBeRemoved));

        boolean isDeleted = this.userServiceToTest.deleteUserEntityByUserId(idOfUserToBeRemoved, userInSessionEmail);

        Assertions
                .assertFalse(isDeleted);
    }


    @Test
    void test_deleteUserEntityByUserId_ShouldReturnReturnTrue() {
        long idOfUserToBeRemoved = 69L;
        String userInSessionEmail = "emal-found@userInSessionEmail.bg";

        UserEntity userInSession = (UserEntity) new UserEntity()
                .setEmail(userInSessionEmail)
                .setId(42L);

        TaskEntity expectedTask = new TaskEntity();
        List<TaskEntity> usersTasks = new ArrayList<>(List.of(expectedTask));

        DepartmentEntity departmentInTest = new DepartmentEntity()
                .setDepartmentName(DepartmentTypeEnum.BACK_END_DEVELOPMENT_TEAM);
        UserEntity userToBeRemoved = (UserEntity) new UserEntity()
                .setEmail(userInSessionEmail)
                .setTasks(usersTasks)
                .setDepartment(departmentInTest)
                .setId(idOfUserToBeRemoved);

        expectedTask.setAssignedUsers(new ArrayList<>(Collections.singletonList(userToBeRemoved)));


        Mockito
                .when(this.mockUserRepository.findFirstByEmail(userInSessionEmail))
                .thenReturn(Optional.of(userInSession));
        Mockito
                .when(this.mockUserRepository.findById(idOfUserToBeRemoved))
                .thenReturn(Optional.of(userToBeRemoved));

        boolean isDeleted = this.userServiceToTest.deleteUserEntityByUserId(idOfUserToBeRemoved, userInSessionEmail);

        Assertions
                .assertTrue(isDeleted);

        Mockito
                .verify(mockDepartmentService, Mockito.times(1))
                .decrementUsersCountInDepartment(userToBeRemoved.getDepartment());

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .deleteById(idOfUserToBeRemoved);

        Assertions
                .assertEquals(0, userToBeRemoved.getTasks().get(0).getAssignedUsers().size());
    }

    @Test
    void test_updateUser_ShouldThrow() {
        Assertions
                .assertThrows(ObjNotFoundException.class, () -> {
                    long userId = 13L;
                    UserDetailsViewDto userDetailsViewDto = new UserDetailsViewDto();
                    this.userServiceToTest.updateUser(userId, userDetailsViewDto);
                });
    }

    @Test
    void test_updateUser_ShouldUpdateSuccessfully() {
        long userId = 69L;
        UserDetailsViewDto userDetailsViewDto = new UserDetailsViewDto()
                .setDepartment(DepartmentTypeEnum.BACK_END_DEVELOPMENT_TEAM);

        DepartmentEntity oldDepartment = new DepartmentEntity()
                .setDepartmentName(DepartmentTypeEnum.FRONT_END_DEVELOPMENT_TEAM)
                .setCount(10);

        UserEntity userToBeUpdatedTest = new UserEntity()
                .setDepartment(oldDepartment)
                .setEmail("test@testov.bg")
                .setFirstName("Test");
        Mockito
                .when(this.mockUserRepository.findById(userId))
                .thenReturn(Optional.of(userToBeUpdatedTest));

        DepartmentEntity newDepartment = new DepartmentEntity()
                .setDepartmentName(userDetailsViewDto.getDepartment());
        Mockito
                .when(this.mockDepartmentService.getDepartmentEntityByTypeEnum(userDetailsViewDto.getDepartment()))
                .thenReturn(newDepartment);

        this.userServiceToTest.updateUser(userId, userDetailsViewDto);

        Assertions
                .assertEquals(newDepartment, userToBeUpdatedTest.getDepartment());
        Mockito
                .verify(mockDepartmentService, Mockito.times(1))
                .decrementUsersCountInDepartment(oldDepartment);
        Mockito
                .verify(mockDepartmentService, Mockito.times(1))
                .incrementUsersCountInDepartment(newDepartment);
        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAndFlush(userToBeUpdatedTest);

        Mockito.verify(this.mockUserRepository, Mockito.times(1))
                .saveAndFlush(this.userEntityArgumentCaptor.capture());
        UserEntity actualEntity = userEntityArgumentCaptor.getValue();

        Assertions
                .assertEquals(userToBeUpdatedTest.hashCode(), actualEntity.hashCode());
        Assertions
                .assertEquals(userToBeUpdatedTest.getEmail(), actualEntity.getEmail());
        Assertions
                .assertEquals(newDepartment, actualEntity.getDepartment());
    }

    @Test
    void test_updateUserRole_ShouldThrow() {
        Assertions
                .assertThrows(ObjNotFoundException.class, () -> {
                    long userId = 13L;
                    RoleTypeEnum role = RoleTypeEnum.ADMIN;
                    this.userServiceToTest.updateUserRole(userId, role);
                });
    }

    @Test
    void test_updateUserRole_ShouldUpdateWithRoles_ADMIN() {
        long userId = 69L;
        RoleTypeEnum adminEnum = RoleTypeEnum.ADMIN;
        RoleTypeEnum moderatorEnum = RoleTypeEnum.MODERATOR;
        RoleTypeEnum userEnum = RoleTypeEnum.USER;

        UserEntity userToBeUpdated = (UserEntity) new UserEntity()
                .setEmail("test@example.bg")
                .setId(userId);

        Mockito
                .when(this.mockUserRepository.findById(userId))
                .thenReturn(Optional.of(userToBeUpdated));

        UserRoleEntity adminRoleEntity = new UserRoleEntity().setRole(adminEnum);
        UserRoleEntity moderatorRoleEntity = new UserRoleEntity().setRole(moderatorEnum);
        UserRoleEntity userRoleEntity = new UserRoleEntity().setRole(userEnum);

        List<UserRoleEntity> userRoles = new ArrayList<>();
        userRoles.add(adminRoleEntity);
        userRoles.add(moderatorRoleEntity);
        userRoles.add(userRoleEntity);

        Mockito
                .when(this.mockUserRoleService.getAdminRoles())
                .thenReturn(userRoles);


        this.userServiceToTest.updateUserRole(userId, adminEnum);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAndFlush(userEntityArgumentCaptor.capture());

        UserEntity updatedUser = userEntityArgumentCaptor.getValue();

        Assertions
                .assertTrue(updatedUser
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Objects.equals(r, adminRoleEntity)));

        Assertions
                .assertTrue(updatedUser
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Objects.equals(r, moderatorRoleEntity)));

        Assertions
                .assertTrue(updatedUser
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Objects.equals(r, userRoleEntity)));
        Assertions
                .assertEquals(3, updatedUser.getRoles().size());
        Assertions
                .assertEquals(userId, updatedUser.getId());
    }


    @Test
    void test_updateUserRole_ShouldUpdateWithRoles_MODERATOR() {
        long userId = 69L;

        RoleTypeEnum moderatorEnum = RoleTypeEnum.MODERATOR;
        RoleTypeEnum userEnum = RoleTypeEnum.USER;

        UserEntity userToBeUpdated = (UserEntity) new UserEntity()
                .setEmail("test@example.bg")
                .setId(userId);

        Mockito
                .when(this.mockUserRepository.findById(userId))
                .thenReturn(Optional.of(userToBeUpdated));

        UserRoleEntity moderatorRoleEntity = new UserRoleEntity().setRole(moderatorEnum);
        UserRoleEntity userRoleEntity = new UserRoleEntity().setRole(userEnum);

        List<UserRoleEntity> userRoles = new ArrayList<>();
        userRoles.add(moderatorRoleEntity);
        userRoles.add(userRoleEntity);

        Mockito
                .when(this.mockUserRoleService.getModeratorRoles())
                .thenReturn(userRoles);


        this.userServiceToTest.updateUserRole(userId, moderatorEnum);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAndFlush(userEntityArgumentCaptor.capture());

        UserEntity updatedUser = userEntityArgumentCaptor.getValue();
        Assertions
                .assertTrue(updatedUser
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Objects.equals(r, moderatorRoleEntity)));
        Assertions
                .assertTrue(updatedUser
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Objects.equals(r, userRoleEntity)));
        Assertions
                .assertEquals(2, updatedUser.getRoles().size());
        Assertions
                .assertEquals(userId, updatedUser.getId());
    }

    @Test
    void test_updateUserRole_ShouldUpdateWithRoles_USER() {
        long userId = 69L;
        RoleTypeEnum userEnum = RoleTypeEnum.USER;

        UserEntity userToBeUpdated = (UserEntity) new UserEntity()
                .setEmail("test@example.bg")
                .setId(userId);

        Mockito
                .when(this.mockUserRepository.findById(userId))
                .thenReturn(Optional.of(userToBeUpdated));

        UserRoleEntity userRoleEntity = new UserRoleEntity()
                .setRole(userEnum);

        List<UserRoleEntity> userRoles = new ArrayList<>();
        userRoles.add(userRoleEntity);

        Mockito
                .when(this.mockUserRoleService.getUserRoles())
                .thenReturn(userRoles);

        this.userServiceToTest.updateUserRole(userId, userEnum);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAndFlush(userEntityArgumentCaptor.capture());

        UserEntity updatedUser = userEntityArgumentCaptor.getValue();

        Assertions
                .assertTrue(updatedUser
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Objects.equals(r, userRoleEntity)));
        Assertions
                .assertEquals(1, updatedUser.getRoles().size());
        Assertions
                .assertEquals(userId, updatedUser.getId());
    }

    @Test
    void test_findAllUsersRestViewsRoleId_ShouldReturnEmptyList() {
        long roleId = 13L;

        Mockito
                .when(this.mockUserRepository.findAllByRole_id(roleId))
                .thenReturn(Optional.empty());

        List<UserBasicRestViewDto> actualView = this.userServiceToTest.findAllUsersRestViewsRoleId(roleId);

        Assertions.assertEquals(new ArrayList<>(), actualView);

    }

    @Test
    void test_findAllUsersRestViewsRoleId_ShouldReturnThemSuccessfully() {
        long roleId = 13L;

        DepartmentEntity firstUserDepartment = new DepartmentEntity().setDepartmentName(DepartmentTypeEnum.BACK_END_DEVELOPMENT_TEAM);
        UserEntity firstUser = new UserEntity().setEmail("test@testov.bg")
                .setDepartment(firstUserDepartment);

        DepartmentEntity secondUserDepartment = new DepartmentEntity().setDepartmentName(DepartmentTypeEnum.FRONT_END_DEVELOPMENT_TEAM);
        UserEntity secondUser = new UserEntity().setEmail("example@testov.bg")
                .setDepartment(secondUserDepartment);
        ;
        List<UserEntity> expectedUsers = new ArrayList<>();
        expectedUsers.add(firstUser);
        expectedUsers.add(secondUser);
        Mockito
                .when(this.mockUserRepository.findAllByRole_id(roleId))
                .thenReturn(Optional.of(expectedUsers));

        Mockito
                .when(mockModelMapper.map(firstUser, UserBasicRestViewDto.class))
                .thenReturn(new UserBasicRestViewDto().setDepartment(firstUserDepartment.getDepartmentName().name()));

        Mockito
                .when(mockModelMapper.map(secondUser, UserBasicRestViewDto.class))
                .thenReturn(new UserBasicRestViewDto().setDepartment(secondUserDepartment.getDepartmentName().name()));

        List<UserBasicRestViewDto> actualUsers = this.userServiceToTest.findAllUsersRestViewsRoleId(roleId);

        Assertions.assertEquals(expectedUsers.size(), actualUsers.size());
        Assertions.assertEquals(firstUserDepartment.getDepartmentName().name(), actualUsers.get(0).getDepartment());
        Assertions.assertEquals(secondUserDepartment.getDepartmentName().name(), actualUsers.get(1).getDepartment());
    }

    @Test
    void test_getUserDetailsViewDtoByEmail_ShouldThrow() {
        Assertions
                .assertThrows(UsernameNotFoundException.class, () -> {
                    String emailNotFound = "email@not-found.bg";
                    this.userServiceToTest.getUserDetailsViewDtoByEmail(emailNotFound);
                });
    }

    @Test
    void test_getUserDetailsViewDtoByEmail_ShouldReturnSuccessfully() {
        String emailFound = "email@found.bg";

        UserEntity expectedUser = new UserEntity().setEmail(emailFound).setFirstName("Test");
        Mockito
                .when(this.mockUserRepository.findFirstByEmail(emailFound))
                .thenReturn(Optional.of(expectedUser));
        Mockito
                .when(mockModelMapper.map(expectedUser, UserDetailsViewDto.class))
                .thenReturn(new UserDetailsViewDto()
                        .setEmail(expectedUser.getEmail())
                        .setFirstName(expectedUser.getFirstName()));

        UserDetailsViewDto actualUser = this.userServiceToTest.getUserDetailsViewDtoByEmail(emailFound);

        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    }

    @Test
    void test_updatePassword_ShouldSaveItSuccessfully() {
        UserEntity userEntity = new UserEntity()
                .setEmail("test@example.bg");
        Mockito
                .when(this.mockUserRepository.saveAndFlush(userEntity))
                .thenReturn(userEntity);

        this.userServiceToTest.saveUserEntity(userEntity);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any());
    }

    @Test
    void test_registerUserEntity_ShouldReturnFalseAsTheEmailIsPresent() {
        String presentEmail = "present@email.bg";

        UserRegisterDto userRegisterDto = new UserRegisterDto()
                .setEmail(presentEmail);
        Mockito
                .when(this.mockUserRepository.findFirstByEmail(userRegisterDto.getEmail()))
                .thenReturn(Optional.of(new UserEntity().setEmail(userRegisterDto.getEmail())));

        boolean isRegistered = this.userServiceToTest.registerUserEntity(userRegisterDto);

        Assertions
                .assertFalse(isRegistered);
    }

    @Test
    void test_registerUserEntity_ShouldReturnFalseAsTheUsernameIsPresent() {

        String presentUsername = "presentUsername";
        UserRegisterDto userRegisterDto = new UserRegisterDto()
                .setUsername(presentUsername);

        Mockito
                .when(this.mockUserRepository.findFirstByUsername(userRegisterDto.getUsername()))
                .thenReturn(Optional.of(new UserEntity().setEmail(userRegisterDto.getUsername())));


        boolean isRegistered = this.userServiceToTest.registerUserEntity(userRegisterDto);

        Assertions
                .assertFalse(isRegistered);
    }

    @Test
    void test_registerUserEntity_ShouldRegisterSuccessfully() {
        String providedPassword = "testPass";
        String encodedPassword = "ENCODED_PASS";
        UserRegisterDto userRegisterDto = new UserRegisterDto()
                .setAge(22)
                .setFirstName("Test")
                .setPassword(providedPassword)
                .setDepartment(DepartmentTypeEnum.BACK_END_DEVELOPMENT_TEAM);

        DepartmentEntity userDepartment = new DepartmentEntity().setDepartmentName(userRegisterDto.getDepartment());
        UserEntity userTobeRegistered = new UserEntity()
                .setAge(userRegisterDto.getAge())
                .setPassword(userRegisterDto.getPassword())
                .setDepartment(userDepartment)
                .setFirstName(userRegisterDto.getFirstName());
        Mockito
                .when(this.mockModelMapper.map(userRegisterDto, UserEntity.class))
                .thenReturn(userTobeRegistered);

        UserRoleEntity userRole = new UserRoleEntity()
                .setRole(RoleTypeEnum.USER);
        Mockito
                .when(this.mockUserRoleService.getUserRoleEntityByRoleType(RoleTypeEnum.USER))
                .thenReturn(userRole);

        Mockito
                .when(this.mockDepartmentService.getDepartmentEntityByTypeEnum(userRegisterDto.getDepartment()))
                .thenReturn(userDepartment);

        Mockito
                .when(this.mockPasswordEncoder.encode(userRegisterDto.getPassword()))
                .thenReturn(encodedPassword);

        boolean isRegistered = this.userServiceToTest.registerUserEntity(userRegisterDto);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAndFlush(userEntityArgumentCaptor.capture());
        UserEntity savedUser = this.userEntityArgumentCaptor.getValue();

        Mockito
                .verify(mockDepartmentService, Mockito.times(1))
                .incrementUsersCountInDepartment(userTobeRegistered.getDepartment());

        Mockito
                .verify(mockEmailService, Mockito.times(1))
                .sendRegistrationEmail(savedUser.getEmail(), savedUser.getFirstName());

        Assertions.assertEquals(encodedPassword, savedUser.getPassword());

        Assertions.assertTrue(isRegistered);
    }

    @Test
    void test_userInitialization_ShouldPopulateDbSuccessfully() {

        DepartmentEntity backEnd = new DepartmentEntity().setDepartmentName(BACK_END_DEVELOPMENT_TEAM);
        Mockito
                .when(this.mockDepartmentService.getDepartmentEntityByTypeEnum(BACK_END_DEVELOPMENT_TEAM))
                .thenReturn(backEnd);

        DepartmentEntity frontEnd = new DepartmentEntity().setDepartmentName(FRONT_END_DEVELOPMENT_TEAM);
        Mockito
                .when(this.mockDepartmentService.getDepartmentEntityByTypeEnum(FRONT_END_DEVELOPMENT_TEAM))
                .thenReturn(frontEnd);

        DepartmentEntity testTeam = new DepartmentEntity().setDepartmentName(TEST_DEVELOPMENT_TEAM);
        Mockito
                .when(this.mockDepartmentService.getDepartmentEntityByTypeEnum(TEST_DEVELOPMENT_TEAM))
                .thenReturn(testTeam);


        RoleTypeEnum adminEnum = RoleTypeEnum.ADMIN;
        RoleTypeEnum moderatorEnum = RoleTypeEnum.MODERATOR;
        RoleTypeEnum userEnum = RoleTypeEnum.USER;
        UserRoleEntity adminRoleEntity = new UserRoleEntity().setRole(adminEnum);
        UserRoleEntity moderatorRoleEntity = new UserRoleEntity().setRole(moderatorEnum);
        UserRoleEntity userRoleEntity = new UserRoleEntity().setRole(userEnum);

        List<UserRoleEntity> adminRoles = new ArrayList<>();
        adminRoles.add(adminRoleEntity);
        adminRoles.add(moderatorRoleEntity);
        adminRoles.add(userRoleEntity);

        List<UserRoleEntity> moderatorRoles = new ArrayList<>();
        moderatorRoles.add(moderatorRoleEntity);
        moderatorRoles.add(userRoleEntity);

        List<UserRoleEntity> userRoles = new ArrayList<>();
        userRoles.add(userRoleEntity);

        Mockito
                .when(this.mockUserRoleService.getAdminRoles())
                .thenReturn(adminRoles);
        Mockito
                .when(this.mockUserRoleService.getModeratorRoles())
                .thenReturn(moderatorRoles);
        Mockito
                .when(this.mockUserRoleService.getUserRoles())
                .thenReturn(userRoles);

        String encodedPass = "ENCODED_PASS";
        Mockito
                .when(mockPasswordEncoder.encode("123"))
                .thenReturn(encodedPass);

        List<UserEntity> allUsersTobeSaved = new ArrayList<>(List.of(
                new UserEntity()
                        .setFirstName("Admin")
                        .setLastName("Adminov")
                        .setUsername("admin")
                        .setEmail("admin@adminov.bg")
                        .setAge(33)
                        .setEnabled(true)
                        .setDepartment(backEnd)
                        .setRoles(adminRoles)
                        .setPassword("123"),
                new UserEntity()
                        .setFirstName("Moderator")
                        .setLastName("Moderatorov")
                        .setUsername("moderator")
                        .setEmail("moderator@moderatorov.bg")
                        .setAge(22)
                        .setEnabled(true)
                        .setDepartment(frontEnd)
                        .setRoles(moderatorRoles)
                        .setPassword("123"),
                new UserEntity()
                        .setFirstName("User")
                        .setLastName("Userov")
                        .setUsername("user")
                        .setEmail("user@userov.bg")
                        .setAge(18)
                        .setEnabled(true)
                        .setDepartment(testTeam)
                        .setRoles(userRoles)
                        .setPassword("123")
        ));


        this.userServiceToTest.userInitialization();

        Mockito
                .verify(mockDepartmentService, Mockito.times(3))
                .incrementUsersCountInDepartment(Mockito.any());

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .saveAllAndFlush(Mockito.any());


    }


}
