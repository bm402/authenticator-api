package com.bncrypted.authenticator.repository;

import com.bncrypted.authenticator.model.UserCredentials;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface AuthDao {

    @SqlQuery(
            "SELECT username, hashed_password AS hashedPassword, mfa_key AS mfaKey " +
            "FROM users " +
            "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials getUserCredentials(String username);

}
