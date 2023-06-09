package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.model.UserRegisterDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.dto.view.rest.UserBasicRestViewDto;
import app.taskmanagementsystem.domain.entity.DepartmentEntity;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.UserRepository;
import app.taskmanagementsystem.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static app.taskmanagementsystem.domain.entity.enums.DepartmentTypeEnum.*;

@Service
public class UserServiceImpl implements UserService, DbInit {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleService userRoleService,
                           DepartmentService departmentService,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.departmentService = departmentService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    @Override
    public boolean isDbInit() {
        return this.userRepository.count() != 0;
    }

    @Override
    public boolean registerUserEntity(UserRegisterDto userRegisterDto) {
        final Optional<UserEntity> userByEmail = this.userRepository.findFirstByEmail(userRegisterDto.getEmail());
        final Optional<UserEntity> userByUsername = this.userRepository.findFirstByUsername(userRegisterDto.getUsername());

        if (userByEmail.isPresent() || userByUsername.isPresent()) {
            return false;
        }

        final UserEntity userEntityToBeSaved = this.modelMapper.map(userRegisterDto, UserEntity.class);
        final UserRoleEntity roleToBeSet = this.userRoleService.getUserRoleEntityByRoleType(RoleTypeEnum.USER);
        final DepartmentEntity departmentByTypeEnum = this.departmentService.getDepartmentEntityByTypeEnum(userRegisterDto.getDepartment());


        userEntityToBeSaved
                .setCreatedOn(LocalDateTime.now())
                .setLastPasswordChangeDate(LocalDateTime.now())
                .setPassword(this.passwordEncoder.encode(userRegisterDto.getPassword()))
                .setEnabled(true)
                .setDepartment(departmentByTypeEnum)
                .setRoles(
                        new ArrayList<>(Collections.singletonList(roleToBeSet)
                        )
                );

        this.userRepository.saveAndFlush(userEntityToBeSaved);
        this.departmentService.incrementUsersCountInDepartment(departmentByTypeEnum);

        this.emailService.sendRegistrationEmail(userEntityToBeSaved.getEmail(), userEntityToBeSaved.getFirstName());
        return true;
    }

    @Override
    public boolean checkIfPasswordsMatch(UserRegisterDto userRegisterDto) {
        return userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword());
    }


    @Override
    public UserEntity getUserEntityByEmail(String email) {
        return this.userRepository
                .findFirstByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found."));
    }

    @Override
    @Transactional
    public List<UserBasicViewDto> findAllUserBasicViewsDto() {
        return this.userRepository.findAll()
                .stream()
                .map(this::fromUserEntityToUserBasicViewDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDetailsViewDto getUserDetailsViewByUserId(Long userId) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ObjNotFoundException();
        }
        return mapUserEntityToUserDetailsViewDto(optionalUser.get());
    }

    @Override
    @Transactional
    public boolean deleteUserEntityByUserId(Long userId,
                                            String email) {
        Optional<UserEntity> optionalUserInSession = this.userRepository.findFirstByEmail(email);
        if (optionalUserInSession.isEmpty()) {
            throw new ObjNotFoundException();
        }
        Optional<UserEntity> optionalUserToBeRemoved = this.userRepository.findById(userId);
        if (optionalUserToBeRemoved.isEmpty()) {
            throw new ObjNotFoundException();
        }

        UserEntity userInSession = optionalUserInSession.get();
        UserEntity userToBeRemoved = optionalUserToBeRemoved.get();

        if (Objects.equals(userInSession.getId(), userToBeRemoved.getId())) {
            return false;
        }
        userToBeRemoved.removeUserFromAllTasks(userToBeRemoved);
        DepartmentEntity department = userToBeRemoved.getDepartment();
        this.departmentService.decrementUsersCountInDepartment(department);
        this.userRepository.deleteById(userId);
        return true;
    }

    @Override
    public void updateUser(Long userId,
                           UserDetailsViewDto userDetailsViewDto) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ObjNotFoundException();
        }

        final UserEntity userToBeUpdated = optionalUser.get();

        final DepartmentEntity oldDepartment = userToBeUpdated.getDepartment();
        this.departmentService.decrementUsersCountInDepartment(oldDepartment);

        final DepartmentEntity departmentToBeSet = this.departmentService.getDepartmentEntityByTypeEnum(userDetailsViewDto.getDepartment());
        userToBeUpdated.setDepartment(departmentToBeSet);

        this.departmentService.incrementUsersCountInDepartment(departmentToBeSet);

        this.userRepository.saveAndFlush(userToBeUpdated);
    }

    @Override
    public void updateUserRole(Long userId,
                               RoleTypeEnum role) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ObjNotFoundException();
        }

        List<UserRoleEntity> rolesToBeSet;
        switch (role) {
            case ADMIN -> rolesToBeSet = this.userRoleService.getAdminRoles();
            case MODERATOR -> rolesToBeSet = this.userRoleService.getModeratorRoles();
            case USER -> rolesToBeSet = this.userRoleService.getUserRoles();
            default -> throw new ObjNotFoundException();
        }

        UserEntity userToBeUpdatedWithNewRoles = optionalUser.get();
        userToBeUpdatedWithNewRoles.changeRoles(rolesToBeSet);
        this.userRepository.saveAndFlush(userToBeUpdatedWithNewRoles);
    }

    @Override
    public List<UserBasicRestViewDto> findAllUsersRestViewsRoleId(Long roleId) {
        Optional<List<UserEntity>> allUsersByRoleId = this.userRepository.findAllByRole_id(roleId);
        if (allUsersByRoleId.isEmpty()) {
            return new ArrayList<>();
        }
        return allUsersByRoleId
                .get()
                .stream()
                .map(this::fromUserEntityToRestView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDetailsViewDto getUserDetailsViewDtoByEmail(String email) {
        UserEntity userEntityByEmail = getUserEntityByEmail(email);
        return mapUserEntityToUserDetailsViewDto(userEntityByEmail);
    }

    @Override
    public void saveUserEntity(UserEntity userEntity) {
        this.userRepository.saveAndFlush(userEntity);
    }

    private UserBasicRestViewDto fromUserEntityToRestView(UserEntity userEntity) {
        return this.modelMapper.map(userEntity, UserBasicRestViewDto.class)
                .setDepartment(userEntity.getDepartment().getDepartmentName().name());
    }

    private UserDetailsViewDto mapUserEntityToUserDetailsViewDto(UserEntity userEntity) {
        return this.modelMapper
                .map(userEntity, UserDetailsViewDto.class);
    }

    private UserBasicViewDto fromUserEntityToUserBasicViewDto(UserEntity userEntity) {
        return this.modelMapper
                .map(userEntity, UserBasicViewDto.class);
    }

    @Override
    public void userInitialization() {

        if (isDbInit()) {
            return;
        }

        DepartmentEntity backEndDepartment = this.departmentService.getDepartmentEntityByTypeEnum(BACK_END_DEVELOPMENT_TEAM);
        DepartmentEntity frontEndDepartment = this.departmentService.getDepartmentEntityByTypeEnum(FRONT_END_DEVELOPMENT_TEAM);
        DepartmentEntity testDevelopmentDepartment = this.departmentService.getDepartmentEntityByTypeEnum(TEST_DEVELOPMENT_TEAM);

        List<UserEntity> allUsersTobeSaved = new ArrayList<>(List.of(
                new UserEntity()
                        .setFirstName("Admin")
                        .setLastName("Adminov")
                        .setUsername("admin")
                        .setEmail("admin@adminov.bg")
                        .setAge(33)
                        .setEnabled(true)
                        .setCreatedOn(LocalDateTime.now())
                        .setLastPasswordChangeDate(LocalDateTime.now())
                        .setDepartment(backEndDepartment)
                        .setRoles(this.userRoleService.getAdminRoles())
                        .setPassword(this.passwordEncoder.encode("123")),
                new UserEntity()
                        .setFirstName("Moderator")
                        .setLastName("Moderatorov")
                        .setUsername("moderator")
                        .setEmail("moderator@moderatorov.bg")
                        .setAge(22)
                        .setEnabled(true)
                        .setCreatedOn(LocalDateTime.now())
                        .setLastPasswordChangeDate(LocalDateTime.now())
                        .setDepartment(frontEndDepartment)
                        .setRoles(this.userRoleService.getModeratorRoles())
                        .setPassword(this.passwordEncoder.encode("123")),
                new UserEntity()
                        .setFirstName("User")
                        .setLastName("Userov")
                        .setUsername("user")
                        .setEmail("user@userov.bg")
                        .setAge(18)
                        .setEnabled(true)
                        .setCreatedOn(LocalDateTime.now())
                        .setLastPasswordChangeDate(LocalDateTime.now())
                        .setDepartment(testDevelopmentDepartment)
                        .setRoles(this.userRoleService.getUserRoles())
                        .setPassword(this.passwordEncoder.encode("123"))
        ));

        this.departmentService.incrementUsersCountInDepartment(backEndDepartment);
        this.departmentService.incrementUsersCountInDepartment(frontEndDepartment);
        this.departmentService.incrementUsersCountInDepartment(testDevelopmentDepartment);


        this.userRepository.saveAllAndFlush(allUsersTobeSaved);
    }
}
