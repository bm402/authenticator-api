package integration.com.bncrypted.authenticator.helper;

import com.bncrypted.authenticator.model.UserAndHashedPassword;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface DatabaseHelperRepository {

    @SqlQuery(
            "INSERT INTO users (username, hashed_password) " +
                    "VALUES (:username, :hashedPassword) " +
                    "RETURNING username"
    )
    String addUser(@BindBean UserAndHashedPassword userAndHashedPassword);

    @SqlQuery(
            "SELECT username, hashed_password AS hashedPassword " +
                    "FROM users " +
                    "WHERE username = :username"
    )
    @RegisterConstructorMapper(UserAndHashedPassword.class)
    UserAndHashedPassword getUser(String username);

}
