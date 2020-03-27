package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.UserAndHashedPassword;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.repository.UserDao;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

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
        UserAndHashedPassword existingUserWithUsername = jdbi.withExtension(UserDao.class,
                dao -> dao.getUser(userAndPassword.getUsername()));
        if (existingUserWithUsername != null) {
            throw new InvalidCredentialsException("Username already taken");
        }
        UserAndHashedPassword userAndHashedPassword = encodePassword(
                userAndPassword.getUsername(), userAndPassword.getPassword());
        String returnedUsername = jdbi.withExtension(UserDao.class, dao -> dao.addUser(userAndHashedPassword));
        return new UserResponse(returnedUsername, "User profile created");
    }

    public UserResponse updateUser(UserAndNewPassword userAndNewPassword) {
        if (!verifyPassword(userAndNewPassword.getUsername(), userAndNewPassword.getOldPassword())) {
            throw new InvalidCredentialsException();
        }
        UserAndHashedPassword userAndHashedPassword = encodePassword(
                userAndNewPassword.getUsername(), userAndNewPassword.getNewPassword());
        String returnedUsername = jdbi.withExtension(UserDao.class, dao -> dao.updateUser(userAndHashedPassword));
        return new UserResponse(returnedUsername, "User profile updated");
    }

    public UserResponse deleteUser(UserAndPassword userAndPassword) {
        if (!verifyPassword(userAndPassword.getUsername(), userAndPassword.getPassword())) {
            throw new InvalidCredentialsException();
        }
        String returnedUsername = jdbi.withExtension(UserDao.class,
                dao -> dao.deleteUser(userAndPassword.getUsername()));
        return new UserResponse(returnedUsername, "User profile deleted");
    }

    private UserAndHashedPassword encodePassword(String username, String password) {
        return new UserAndHashedPassword(username, passwordEncoder.encode(password));
    }

    private boolean verifyPassword(String username, String password) {
        UserAndHashedPassword existingUserAndHashedPassword = jdbi.withExtension(
                UserDao.class, dao -> dao.getUser(username));
        if (existingUserAndHashedPassword == null) {
            throw new InvalidCredentialsException();
        }
        String storedHashedPassword = existingUserAndHashedPassword.getHashedPassword();
        return passwordEncoder.matches(password, storedHashedPassword);
    }

}
