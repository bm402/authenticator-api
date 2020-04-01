package com.bncrypted.authenticator.repository;

import com.bncrypted.authenticator.model.UserAndHashedPassword;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface UserDao {

    @SqlQuery(
            "INSERT INTO users (username, hashed_password, mfa_key) " +
            "VALUES (:username, :hashedPassword, :mfaKey) " +
            "RETURNING username"
    )
    String addUser(@BindBean UserAndHashedPassword userAndHashedPassword, String mfaKey);

    @SqlQuery(
            "UPDATE users " +
            "SET hashed_password = :hashedPassword " +
            "WHERE username = :username " +
            "RETURNING username"
    )
    String updateUser(@BindBean UserAndHashedPassword userAndHashedPassword);

    @SqlQuery(
            "DELETE FROM users " +
            "WHERE username = :username " +
            "RETURNING username"
    )
    String deleteUser(String username);

    @SqlQuery(
            "SELECT username, hashed_password AS hashedPassword " +
            "FROM users " +
            "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserAndHashedPassword.class)
    UserAndHashedPassword getUser(String username);

}
