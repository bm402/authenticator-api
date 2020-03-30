package integration.com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.UserAndHashedPassword;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.service.UserService;
import com.bncrypted.authenticator.service.UserServiceImpl;
import integration.com.bncrypted.authenticator.base.IntegrationBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest extends IntegrationBaseTest {

    private static final UserService userService = initService(UserServiceImpl.class);
    private String username;
    private String existingPassword;
    private UserAndHashedPassword existingCredentials;

    @BeforeEach
    void setup() {
        username = UUID.randomUUID().toString();
        existingPassword = UUID.randomUUID().toString();
        existingCredentials = new UserAndHashedPassword(username, passwordEncoder.encode(existingPassword));
        databaseHelper.addUser(existingCredentials);
    }

    @Test
    void whenAddingUserThatDoesNotAlreadyExist_thenUserShouldBeAdded() {
        String newUsername = UUID.randomUUID().toString();
        UserAndPassword newCredentials = new UserAndPassword(newUsername, UUID.randomUUID().toString());

        UserResponse expectedResponse = new UserResponse(newUsername, "User profile created");
        UserResponse actualResponse = userService.addUser(newCredentials);
        assertThat(actualResponse).isEqualToComparingFieldByField(expectedResponse);

        UserAndHashedPassword credentialsFromDb = databaseHelper.getUser(newUsername);
        assertNotNull(credentialsFromDb);
        assertEquals(newUsername, credentialsFromDb.getUsername());
    }

    @Test
    void whenAddingUserThatAlreadyExists_thenUserShouldBeNotBeAdded() {
        UserAndPassword newCredentials = new UserAndPassword(username, UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.addUser(newCredentials));
        assertEquals("Username already taken", actualException.getMessage());

        UserAndHashedPassword credentialsFromDb = databaseHelper.getUser(username);
        assertThat(credentialsFromDb).isEqualToComparingFieldByField(existingCredentials);
    }

    @Test
    void whenUpdatingUserPasswordWithValidOldPassword_thenPasswordShouldBeUpdated() {
        String newPassword = UUID.randomUUID().toString();
        UserAndNewPassword newCredentials = new UserAndNewPassword(username, existingPassword, newPassword);

        UserResponse expectedResponse = new UserResponse(username, "User profile updated");
        UserResponse actualResponse = userService.updateUser(newCredentials);
        assertThat(actualResponse).isEqualToComparingFieldByField(expectedResponse);

        UserAndHashedPassword credentialsFromDb = databaseHelper.getUser(username);
        assertNotNull(credentialsFromDb);
        assertTrue(passwordEncoder.matches(newPassword, credentialsFromDb.getHashedPassword()));
    }

    @Test
    void whenUpdatingUserPasswordWithInvalidOldPassword_thenPasswordShouldNotBeUpdated() {
        UserAndNewPassword newCredentials = new UserAndNewPassword(username, UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.updateUser(newCredentials));
        assertEquals("Invalid credentials", actualException.getMessage());

        UserAndHashedPassword credentialsFromDb = databaseHelper.getUser(username);
        assertNotNull(credentialsFromDb);
        assertTrue(passwordEncoder.matches(existingPassword, credentialsFromDb.getHashedPassword()));
    }

    @Test
    void whenDeletingUserWithValidPassword_thenUserShouldBeDeleted() {
        UserAndPassword validCredentials = new UserAndPassword(username, existingPassword);

        UserResponse expectedResponse = new UserResponse(username, "User profile deleted");
        UserResponse actualResponse = userService.deleteUser(validCredentials);
        assertThat(actualResponse).isEqualToComparingFieldByField(expectedResponse);

        UserAndHashedPassword credentialsFromDb = databaseHelper.getUser(username);
        assertNull(credentialsFromDb);
    }

    @Test
    void whenDeletingUserWithInvalidPassword_thenUserShouldNotBeDeleted() {
        UserAndPassword invalidCredentials = new UserAndPassword(username, UUID.randomUUID().toString());

        InvalidCredentialsException actualException = assertThrows(
                InvalidCredentialsException.class, () -> userService.deleteUser(invalidCredentials));
        assertEquals("Invalid credentials", actualException.getMessage());

        UserAndHashedPassword credentialsFromDb = databaseHelper.getUser(username);
        assertNotNull(credentialsFromDb);
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
