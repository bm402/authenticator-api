package com.bncrypted.authenticator.repository;

import com.bncrypted.authenticator.model.UserCredentials;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDao {

    @SqlQuery(
            "INSERT INTO users (username, hashed_password, mfa_key) " +
            "VALUES (:username, :hashedPassword, :mfaKey) " +
            "RETURNING *"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials addUser(@BindBean UserCredentials userCredentials);

    @SqlUpdate(
            "UPDATE users " +
            "SET hashed_password = :hashedPassword, mfa_key = :mfaKey " +
            "WHERE username = :username"
    )
    void updateUser(@BindBean UserCredentials userCredentials);

    @SqlUpdate(
            "DELETE FROM users " +
            "WHERE username = :username"
    )
    void deleteUser(String username);

    @SqlQuery(
            "SELECT id, username, hashed_password AS hashedPassword, mfa_key AS mfaKey " +
            "FROM users " +
            "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials getUser(String username);

}
