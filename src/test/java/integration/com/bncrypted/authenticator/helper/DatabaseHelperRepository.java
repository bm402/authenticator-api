package integration.com.bncrypted.authenticator.helper;

import com.bncrypted.authenticator.model.UserCredentials;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;

public interface DatabaseHelperRepository {

    @SqlQuery(
            "INSERT INTO users (username, hashed_password, mfa_key) " +
            "VALUES (:username, :hashedPassword, :mfaKey) " +
            "RETURNING *"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials addUser(@BindBean UserCredentials userCredentials);

    @SqlQuery(
            "SELECT id, username, hashed_password AS hashedPassword, mfa_key AS mfaKey " +
            "FROM users " +
            "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserCredentials.class)
    UserCredentials getUser(String username);

    @SqlUpdate(
            "INSERT INTO user_roles " +
            "SELECT id AS user_id, :role AS role " +
            "FROM users " +
            "WHERE username = :username"
    )
    void addUserRole(String username, String role);

    @SqlQuery(
            "SELECT ur.role " +
            "FROM users u, user_roles ur " +
            "WHERE u.username = :username " +
            "AND u.id = ur.user_id"
    )
    Set<String> getUserRoles(String username);

}
