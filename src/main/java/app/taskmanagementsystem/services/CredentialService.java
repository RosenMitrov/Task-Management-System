package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.UserChangePasswordDto;

public interface CredentialService {

    boolean isCredentialsExpired(String email);

    boolean checkIfNewPasswordsMatch(UserChangePasswordDto userChangePasswordDto);

    boolean checkIfPasswordMatchToLastOne(UserChangePasswordDto userChangePasswordDto);

    void changePassword(UserChangePasswordDto userChangePasswordDto);

    boolean checkIfOldPasswordMatchToProvidedPassword(UserChangePasswordDto userChangePasswordDto);
}
