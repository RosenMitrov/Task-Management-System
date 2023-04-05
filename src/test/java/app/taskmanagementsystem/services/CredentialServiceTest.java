package app.taskmanagementsystem.services;

import app.taskmanagementsystem.domain.dto.model.UserChangePasswordDto;
import app.taskmanagementsystem.domain.entity.UserEntity;
import app.taskmanagementsystem.services.impl.CredentialServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class CredentialServiceTest {

    private CredentialService credentialServiceToTest;
    @Mock
    private UserService mockUserService;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void setUp() {
        this.credentialServiceToTest = new CredentialServiceImpl(mockUserService, mockPasswordEncoder);
    }

    @Test
    void test_checkIfPasswordsMatch_ShouldReturnTrue() {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setNewPassword("topsecret")
                .setConfirmNewPassword("topsecret");

        boolean isMatch = this.credentialServiceToTest.checkIfNewPasswordsMatch(userChangePasswordDto);

        Assertions.assertTrue(isMatch);
    }

    @Test
    void test_checkIfPasswordsMatch_ShouldReturnFalse() {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setNewPassword("topsecret")
                .setConfirmNewPassword("notMatch");

        boolean isMatch = this.credentialServiceToTest.checkIfNewPasswordsMatch(userChangePasswordDto);

        Assertions.assertFalse(isMatch);
    }

    @Test
    void test_checkIfPasswordMatchToLastOne_ShouldReturnTrue() {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setEmail("test@example.bg")
                .setNewPassword("topsecret")
                .setConfirmNewPassword("topsecret");

        String encodedPassword = "ENCODED_PASSWORD";

        Mockito
                .when(mockPasswordEncoder.encode(userChangePasswordDto.getNewPassword()))
                .thenReturn(encodedPassword);

        UserEntity expectedUser = new UserEntity()
                .setEmail(userChangePasswordDto.getEmail())
                .setPassword(this.mockPasswordEncoder.encode(userChangePasswordDto.getNewPassword()));

        Mockito
                .when(this.mockUserService.getUserEntityByEmail(userChangePasswordDto.getEmail()))
                .thenReturn(expectedUser);
        Mockito
                .when(this.mockPasswordEncoder.matches(userChangePasswordDto.getNewPassword(), expectedUser.getPassword()))
                .thenReturn(encodedPassword.equals(expectedUser.getPassword()));


        boolean isMatchLastOne = this.credentialServiceToTest.checkIfPasswordMatchToLastOne(userChangePasswordDto);

        Assertions.assertTrue(isMatchLastOne);
    }


    @Test
    void test_checkIfPasswordMatchToLastOne_ShouldReturnFalse() {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setEmail("test@example.bg")
                .setNewPassword("topsecret")
                .setConfirmNewPassword("topsecret");

        String encodedPassword = "ENCODED_PASSWORD";

        UserEntity expectedUser = new UserEntity()
                .setEmail(userChangePasswordDto.getEmail())
                .setPassword("LAST_USER_PASS");

        Mockito
                .when(this.mockUserService.getUserEntityByEmail(userChangePasswordDto.getEmail()))
                .thenReturn(expectedUser);

        Mockito
                .when(this.mockPasswordEncoder.matches(userChangePasswordDto.getNewPassword(), expectedUser.getPassword()))
                .thenReturn(encodedPassword.equals(expectedUser.getPassword()));


        boolean isMatchLastOne = this.credentialServiceToTest.checkIfPasswordMatchToLastOne(userChangePasswordDto);

        Assertions.assertFalse(isMatchLastOne);
    }

    @Test
    void test_changePassword_ShouldChangeItSuccessfully() {
        String newPassword = "NEW_PASSWORD";
        String expectedEncodedPassword = "ENCODED_PASSWORD";
        String oldPassword = "OLD_PASSWORD";

        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setEmail("test@email.bg")
                .setNewPassword(newPassword);

        Mockito
                .when(mockPasswordEncoder.encode(newPassword))
                .thenReturn(expectedEncodedPassword);

        UserEntity userEntityToBeChangedPassword = new UserEntity()
                .setEmail(userChangePasswordDto.getEmail())
                .setPassword(oldPassword);
        Mockito
                .when(this.mockUserService.getUserEntityByEmail(userChangePasswordDto.getEmail()))
                .thenReturn(userEntityToBeChangedPassword);

        this.credentialServiceToTest.changePassword(userChangePasswordDto);

        Mockito
                .verify(mockUserService, Mockito.times(1))
                .saveUserEntity(userEntityToBeChangedPassword);

        Assertions.assertEquals(expectedEncodedPassword, userEntityToBeChangedPassword.getPassword());
    }


    @Test
    void test_isCredentialsExpired_ShouldReturnTrue() {
        String email = "test@example.bg";

        UserEntity userEntity = new UserEntity()
                .setLastPasswordChangeDate(LocalDateTime.now().minusDays(10));
        Mockito
                .when(this.mockUserService.getUserEntityByEmail(email))
                .thenReturn(userEntity);

        boolean isExpired = this.credentialServiceToTest.isCredentialsExpired(email);

        Assertions.assertTrue(isExpired);
    }

    @Test
    void test_isCredentialsExpired_ShouldReturnFalse() {
        String email = "test@example.bg";

        UserEntity userEntity = new UserEntity()
                .setLastPasswordChangeDate(LocalDateTime.now());
        Mockito
                .when(this.mockUserService.getUserEntityByEmail(email))
                .thenReturn(userEntity);

        boolean isExpired = this.credentialServiceToTest.isCredentialsExpired(email);

        Assertions.assertFalse(isExpired);
    }

    @Test
    void test_checkIfOldPasswordMatchToProvidedPassword_ShouldReturnTrue() {
        String encodedPassword = "ENCODED_PASSWORD";

        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setEmail("test@testov.bg")
                .setOldPassword(encodedPassword);

        UserEntity expectedUser = new UserEntity()
                .setPassword(encodedPassword);

        Mockito
                .when(this.mockUserService.getUserEntityByEmail(userChangePasswordDto.getEmail()))
                .thenReturn(expectedUser);

        Mockito
                .when(this.mockPasswordEncoder.matches(userChangePasswordDto.getOldPassword(), expectedUser.getPassword()))
                .thenReturn(userChangePasswordDto.getOldPassword().equals(expectedUser.getPassword()));

        boolean isMatched = this.credentialServiceToTest.checkIfOldPasswordMatchToProvidedPassword(userChangePasswordDto);

        Assertions
                .assertTrue(isMatched);
    }



    @Test
    void test_checkIfOldPasswordMatchToProvidedPassword_ShouldReturnFalse() {
        String providedPassword = "ENCODED_PASSWORD";
        String userPassword = "ENCODED_USER_PASSWORD";

        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto()
                .setEmail("test@testov.bg")
                .setOldPassword(providedPassword);

        UserEntity expectedUser = new UserEntity()
                .setPassword(userPassword);

        Mockito
                .when(this.mockUserService.getUserEntityByEmail(userChangePasswordDto.getEmail()))
                .thenReturn(expectedUser);

        Mockito
                .when(this.mockPasswordEncoder.matches(userChangePasswordDto.getOldPassword(), expectedUser.getPassword()))
                .thenReturn(userChangePasswordDto.getOldPassword().equals(expectedUser.getPassword()));

        boolean isMatched = this.credentialServiceToTest.checkIfOldPasswordMatchToProvidedPassword(userChangePasswordDto);

        Assertions
                .assertFalse(isMatched);
    }
}
