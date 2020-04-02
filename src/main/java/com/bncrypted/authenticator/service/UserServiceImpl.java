package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.repository.UserDao;
import com.google.common.io.BaseEncoding;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final Jdbi jdbi;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbi = Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin());
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse addUser(UserAndPassword userAndPassword) {
        UserCredentials storedUserCredentials = getStoredUserCredentials(userAndPassword.getUsername());
        if (storedUserCredentials != null) {
            throw new InvalidCredentialsException("Username already taken");
        }
        UserCredentials newUserCredentials = createUserCredentials(userAndPassword.getUsername(),
                userAndPassword.getPassword());
        String returnedUsername = jdbi.withExtension(UserDao.class, dao -> dao.addUser(newUserCredentials));
        return new UserResponse(returnedUsername, "User profile created");
    }

    public UserResponse updateUserPassword(UserAndNewPassword userAndNewPassword) {
        UserCredentials storedUserCredentials = getAndVerifyStoredUserCredentials(userAndNewPassword.getUsername(),
                userAndNewPassword.getOldPassword());
        UserCredentials newUserCredentials = new UserCredentials(storedUserCredentials.getUsername(),
                passwordEncoder.encode(userAndNewPassword.getNewPassword()), storedUserCredentials.getMfaKey());
        String returnedUsername = jdbi.withExtension(UserDao.class, dao -> dao.updateUser(newUserCredentials));
        return new UserResponse(returnedUsername, "User password updated");
    }

    public UserResponse updateUserMfaKey(UserAndPassword userAndPassword) {
        UserCredentials storedUserCredentials = getAndVerifyStoredUserCredentials(userAndPassword.getUsername(),
                userAndPassword.getPassword());
        UserCredentials newUserCredentials = createUserCredentials(storedUserCredentials.getUsername(),
                storedUserCredentials.getHashedPassword());
        String returnedUsername = jdbi.withExtension(UserDao.class, dao -> dao.updateUser(newUserCredentials));
        return new UserResponse(returnedUsername, "User MFA key updated");
    }

    public UserResponse deleteUser(UserAndPassword userAndPassword) {
        getAndVerifyStoredUserCredentials(userAndPassword.getUsername(), userAndPassword.getPassword());
        String returnedUsername = jdbi.withExtension(UserDao.class,
                dao -> dao.deleteUser(userAndPassword.getUsername()));
        return new UserResponse(returnedUsername, "User profile deleted");
    }

    private UserCredentials createUserCredentials(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        String mfaKey = BaseEncoding.base32().encode(UUID.randomUUID().toString().getBytes());
        return new UserCredentials(username, encodedPassword, mfaKey);
    }

    private UserCredentials getStoredUserCredentials(String username) {
        return jdbi.withExtension(UserDao.class, dao -> dao.getUser(username));
    }

    private UserCredentials getAndVerifyStoredUserCredentials(String username, String password) {
        UserCredentials storedUserCredentials = getStoredUserCredentials(username);
        if (storedUserCredentials == null ||
                !passwordEncoder.matches(password, storedUserCredentials.getHashedPassword())) {
            throw new InvalidCredentialsException();
        }
        return storedUserCredentials;
    }

}
