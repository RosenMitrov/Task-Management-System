package app.taskmanagementsystem.services.impl;

import app.taskmanagementsystem.domain.dto.model.UserChangePasswordDto;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.services.CredentialService;
import app.taskmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CredentialServiceImpl(UserService userService,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isCredentialsExpired(String email) {
        UserEntity userEntityByEmail = this.userService.getUserEntityByEmail(email);
        LocalDateTime lastChanges = userEntityByEmail.getLastPasswordChangeDate();
        LocalDateTime oneMinutesAgo = LocalDateTime.now().minusMinutes(1);
        if (oneMinutesAgo.isAfter(lastChanges)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfPasswordsMatch(UserChangePasswordDto userChangePasswordDto) {
        return userChangePasswordDto.getNewPassword().equals(userChangePasswordDto.getConfirmNewPassword());
    }

    @Override
    public boolean checkIfPasswordMatchToLastOne(UserChangePasswordDto userChangePasswordDto) {
        UserEntity userEntityByEmail = this.userService.getUserEntityByEmail(userChangePasswordDto.getEmail());
        boolean matches = this.passwordEncoder.matches(userChangePasswordDto.getNewPassword(), userEntityByEmail.getPassword());
        return matches;

    }

    @Override
    public void changePassword(UserChangePasswordDto userChangePasswordDto) {
        UserEntity userEntityByEmail = this.userService.getUserEntityByEmail(userChangePasswordDto.getEmail());
        userEntityByEmail.setPassword(this.passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        userEntityByEmail.setLastPasswordChangeDate(LocalDateTime.now());
        this.userService.updatePassword(userEntityByEmail);
    }
}
