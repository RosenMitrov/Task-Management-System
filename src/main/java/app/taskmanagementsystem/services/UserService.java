package app.taskmanagementsystem.services;


import app.taskmanagementsystem.domain.dto.model.UserRegisterDto;
import app.taskmanagementsystem.domain.dto.view.UserBasicViewDto;
import app.taskmanagementsystem.domain.dto.view.UserDetailsViewDto;
import app.taskmanagementsystem.domain.dto.view.rest.UserBasicRestViewDto;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.domain.entity.enums.RoleTypeEnum;

import java.util.List;

public interface UserService {
    void userInitialization();

    boolean registerUserEntity(UserRegisterDto userRegisterDto);

    boolean checkIfPasswordsMatch(UserRegisterDto userRegisterDto);

    UserEntity getUserEntityByEmail(String email);

    List<UserBasicViewDto> findAllUserBasicViewsDto();

    UserDetailsViewDto getUserDetailsViewByUserId(Long userId);

    boolean deleteUserEntityByUserId(Long userId, String email);

    void updateUser(Long userId,
                    UserDetailsViewDto userDetailsViewDto);

    void updateUserRole(Long userId,
                        RoleTypeEnum role);


    List<UserBasicRestViewDto> findAllUsersRestViewsRoleId(Long roleId);

    UserDetailsViewDto getUserDetailsViewDtoByEmail(String email);

    void updatePassword(UserEntity userEntityByEmail);
}
