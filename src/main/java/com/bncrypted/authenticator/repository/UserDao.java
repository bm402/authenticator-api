package com.bncrypted.authenticator.repository;

import com.bncrypted.authenticator.model.UserAndHashedPassword;
import com.bncrypted.authenticator.model.UserCredentials;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface UserDao {

    @SqlQuery(
            "INSERT INTO users (username, hashed_password, mfa_key) " +
            "VALUES (:username, :hashedPassword, :mfaKey) " +
            "RETURNING username"
    )
    String addUser(@BindBean UserCredentials userCredentials);

    @SqlQuery(
            "UPDATE users " +
            "SET hashed_password = :hashedPassword, mfa_key = :mfaKey " +
            "WHERE username = :username " +
            "RETURNING username"
    )
    String updateUser(@BindBean UserCredentials userCredentials);

    @SqlQuery(
            "DELETE FROM users " +
            "WHERE username = :username " +
            "RETURNING username"
    )
    String deleteUser(String username);

    @SqlQuery(
            "SELECT username, hashed_password AS hashedPassword, mfa_key AS mfaKey " +
            "FROM users " +
            "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials getUser(String username);

}
