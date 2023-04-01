package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.view.rest.RoleRestViewDto;
import app.taskmanagementsystem.domain.entity.UserRoleEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;
import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import app.taskmanagementsystem.init.DbInit;
import app.taskmanagementsystem.repositories.UserRoleRepository;
import app.taskmanagementsystem.services.UserRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum.*;

@Service
public class UserRoleServiceImpl implements UserRoleService, DbInit {


    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository,
                               ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean isDbInit() {
        return this.userRoleRepository.count() != 0;
    }

    @Override
    public UserRoleEntity getUserRoleEntityByRoleType(RoleTypeEnum roleTypeEnum) {
        Optional<UserRoleEntity> firstByRole = this.userRoleRepository.findFirstByRole(roleTypeEnum);

        if (firstByRole.isEmpty()) {
            throw new ObjNotFoundException();
        }

        return firstByRole.get();
    }

    @Override
    public void userRolesInitialization() {
        if (isDbInit()) {
            return;
        }
        List<UserRoleEntity> allUserRoles = Arrays.stream(RoleTypeEnum.values())
                .map(this::formRoleTypeEnumToRoleEntity)
                .toList();
        this.userRoleRepository.saveAllAndFlush(allUserRoles);
    }

    @Override
    public List<UserRoleEntity> getAdminRoles() {
        UserRoleEntity adminRole = getRoleByType(ADMIN);
        UserRoleEntity moderatorRole = getRoleByType(MODERATOR);
        UserRoleEntity userRole = getRoleByType(USER);
        return List.of(
                adminRole, moderatorRole, userRole
        );
    }

    @Override
    public List<UserRoleEntity> getModeratorRoles() {
        UserRoleEntity moderatorRole = getRoleByType(MODERATOR);
        UserRoleEntity userRole = getRoleByType(USER);
        return List.of(
                moderatorRole, userRole
        );
    }

    @Override
    public List<UserRoleEntity> getUserRoles() {
        UserRoleEntity userRole = getRoleByType(USER);
        return List.of(
                userRole
        );
    }

    @Override
    @Transactional
    public List<RoleRestViewDto> allRolesStats() {

        List<UserRoleEntity> allRoles = this.userRoleRepository.findAll();
        return allRoles
                .stream()
                .map(this::fromRoleEntityToRoleView)
                .collect(Collectors.toList());
    }

    private RoleRestViewDto fromRoleEntityToRoleView(UserRoleEntity userRoleEntity) {
        return this.modelMapper
                .map(userRoleEntity, RoleRestViewDto.class)
                .setCountUsers(userRoleEntity.getUsers().size());
    }

    private UserRoleEntity getRoleByType(RoleTypeEnum roleTypeEnum) {
        Optional<UserRoleEntity> firstByRole = this.userRoleRepository.findFirstByRole(roleTypeEnum);
        if (firstByRole.isEmpty()) {
            throw new ObjNotFoundException();
        }
        return firstByRole.get();
    }

    private UserRoleEntity formRoleTypeEnumToRoleEntity(RoleTypeEnum roleTypeEnum) {
        return new UserRoleEntity()
                .setRole(roleTypeEnum)
                .setDescription("Some description about " + roleTypeEnum.name());
    }
}
