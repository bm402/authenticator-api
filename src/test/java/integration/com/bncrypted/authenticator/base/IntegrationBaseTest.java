package integration.com.bncrypted.authenticator.base;

import com.google.common.collect.ImmutableMap;
import integration.com.bncrypted.authenticator.helper.DatabaseHelperRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class IntegrationBaseTest {

    protected static final DatabaseHelperRepository databaseHelper;
    protected static final PasswordEncoder passwordEncoder;
    protected static final PGSimpleDataSource dataSource;

    static {
        PostgreSQLContainer db = createDatabaseContainer();
        db.start();

        dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{db.getContainerIpAddress()});
        dataSource.setPortNumbers(new int[]{db.getFirstMappedPort()});
        dataSource.setDatabaseName(db.getDatabaseName());
        dataSource.setUser(db.getUsername());
        dataSource.setPassword(db.getPassword());
        passwordEncoder = new BCryptPasswordEncoder();

        initSchema();
        databaseHelper = Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin())
                .onDemand(DatabaseHelperRepository.class);
    }

    private static PostgreSQLContainer createDatabaseContainer() {
        return (PostgreSQLContainer) new PostgreSQLContainer("postgres:10-alpine")
                .withUsername("sa")
                .withPassword("sa")
                .withTmpFs(ImmutableMap.of("/var/lib/postgresql/data", "rw"));
    }

    private static void initSchema() {
        FluentConfiguration configuration = new FluentConfiguration();
        configuration.dataSource(dataSource);
        configuration.locations("filesystem:src/main/resources/db/migration");
        Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }

}
