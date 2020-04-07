package integration.com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.UserAndMfaKeyResponse;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.service.UserService;
import com.bncrypted.authenticator.service.UserServiceImpl;
import com.google.common.io.BaseEncoding;
import integration.com.bncrypted.authenticator.base.IntegrationBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest extends IntegrationBaseTest {

    private final UserService userService = new UserServiceImpl(dataSource, passwordEncoder);
    private String username;
    private String existingPassword;
    private String existingMfaKey;
    private UserCredentials existingUserCredentials;

    @BeforeEach
    void setup() {
        username = UUID.randomUUID().toString();
        existingPassword = UUID.randomUUID().toString();
        existingMfaKey = BaseEncoding.base32().encode(UUID.randomUUID().toString().getBytes());
        existingUserCredentials = new UserCredentials(username,
                passwordEncoder.encode(existingPassword), existingMfaKey);
        existingUserCredentials = databaseHelper.addUser(existingUserCredentials);
    }

    @Test
    void whenAddingUserThatDoesNotAlreadyExist_thenUserShouldBeAdded() {
        String newUsername = UUID.randomUUID().toString();
        UserAndPassword newCredentials = new UserAndPassword(newUsername, UUID.randomUUID().toString());

        UserAndMfaKeyResponse expectedResponse = new UserAndMfaKeyResponse(newUsername,
                null, "User profile created");
        UserAndMfaKeyResponse actualResponse = userService.addUser(newCredentials);
        assertAll("UserAndMfaKeyResponse",
                () -> assertEquals(expectedResponse.getUsername(), actualResponse.getUsername()),
                () -> assertNotNull(actualResponse.getMfaKey()),
                () -> assertEquals(expectedResponse.getMessage(), actualResponse.getMessage())
        );

        UserCredentials storedUserCredentials = databaseHelper.getUser(newUsername);
        assertNotNull(storedUserCredentials);
        assertEquals(newUsername, storedUserCredentials.getUsername());
    }

    @Test
    void whenAddingUserThatAlreadyExists_thenUserShouldBeNotBeAdded() {
        UserAndPassword newCredentials = new UserAndPassword(username, UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.addUser(newCredentials));
        assertEquals("Username already taken", actualException.getMessage());

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertThat(storedUserCredentials).isEqualToComparingFieldByField(existingUserCredentials);
    }

    @Test
    void whenUpdatingUserPasswordWithValidOldPassword_thenPasswordShouldBeUpdated() {
        String newPassword = UUID.randomUUID().toString();
        UserAndNewPassword newCredentials = new UserAndNewPassword(username, existingPassword, newPassword);

        UserResponse expectedResponse = new UserResponse(username, "User password updated");
        UserResponse actualResponse = userService.updateUserPassword(newCredentials);
        assertThat(actualResponse).isEqualToComparingFieldByField(expectedResponse);

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertNotNull(storedUserCredentials);
        assertTrue(passwordEncoder.matches(newPassword, storedUserCredentials.getHashedPassword()));
    }

    @Test
    void whenUpdatingUserPasswordWithInvalidOldPassword_thenPasswordShouldNotBeUpdated() {
        UserAndNewPassword invalidCredentials = new UserAndNewPassword(username, UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.updateUserPassword(invalidCredentials));
        assertEquals("Invalid credentials", actualException.getMessage());

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertNotNull(storedUserCredentials);
        assertTrue(passwordEncoder.matches(existingPassword, storedUserCredentials.getHashedPassword()));
    }

    @Test
    void whenUpdatingUserMfaKeyWithValidPassword_thenMfaKeyShouldBeRegenerated() {
        UserAndPassword validCredentials = new UserAndPassword(username, existingPassword);

        UserAndMfaKeyResponse expectedResponse = new UserAndMfaKeyResponse(username,
                null, "User MFA key updated");
        UserAndMfaKeyResponse actualResponse = userService.updateUserMfaKey(validCredentials);
        assertAll("UserAndMfaKeyResponse",
                () -> assertEquals(expectedResponse.getUsername(), actualResponse.getUsername()),
                () -> assertNotNull(actualResponse.getMfaKey()),
                () -> assertEquals(expectedResponse.getMessage(), actualResponse.getMessage())
        );

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertNotNull(storedUserCredentials);
        assertNotEquals(existingMfaKey, storedUserCredentials.getMfaKey());
    }

    @Test
    void whenUpdatingUserMfaKeyWithInvalidPassword_thenMfaKeyShouldNotBeRegenerated() {
        UserAndPassword invalidCredentials = new UserAndPassword(username, UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.updateUserMfaKey(invalidCredentials));
        assertEquals("Invalid credentials", actualException.getMessage());

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertNotNull(storedUserCredentials);
        assertEquals(existingMfaKey, storedUserCredentials.getMfaKey());
    }

    @Test
    void whenDeletingUserWithValidPassword_thenUserShouldBeDeleted() {
        UserAndPassword validCredentials = new UserAndPassword(username, existingPassword);

        UserResponse expectedResponse = new UserResponse(username, "User profile deleted");
        UserResponse actualResponse = userService.deleteUser(validCredentials);
        assertThat(actualResponse).isEqualToComparingFieldByField(expectedResponse);

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertNull(storedUserCredentials);
    }

    @Test
    void whenDeletingUserWithInvalidPassword_thenUserShouldNotBeDeleted() {
        UserAndPassword invalidCredentials = new UserAndPassword(username, UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.deleteUser(invalidCredentials));
        assertEquals("Invalid credentials", actualException.getMessage());

        UserCredentials storedUserCredentials = databaseHelper.getUser(username);
        assertNotNull(storedUserCredentials);
    }

    @Test
    void whenDeletingUserThatDoesNotExist_thenErrorShouldBeThrown() {
        UserAndPassword invalidCredentials = new UserAndPassword(UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.deleteUser(invalidCredentials));
        assertEquals("Invalid credentials", actualException.getMessage());
    }

}
