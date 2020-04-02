package integration.com.bncrypted.authenticator.helper;

import com.bncrypted.authenticator.model.UserAndHashedPassword;
import com.bncrypted.authenticator.model.UserCredentials;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface DatabaseHelperRepository {

    @SqlQuery(
            "INSERT INTO users (username, hashed_password, mfa_key) " +
            "VALUES (:username, :hashedPassword, :mfaKey) " +
            "RETURNING username"
    )
    String addUser(@BindBean UserCredentials userCredentials);

    @SqlQuery(
            "SELECT username, hashed_password AS hashedPassword, mfa_key AS mfaKey " +
            "FROM users " +
            "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials getUser(String username);

    @SqlQuery(
            "SELECT mfa_key " +
            "FROM users " +
            "WHERE username = :username"
    )
    String getMfaKeyForUser(String username);

}
