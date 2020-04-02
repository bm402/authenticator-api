package integration.com.bncrypted.authenticator.configuration;

import com.bncrypted.authenticator.configuration.DataSourceConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = DataSourceConfiguration.class)
@TestPropertySource("classpath:test.properties")
public class DataSourceConfigurationTest {

    @Autowired
    private DataSourceConfiguration dataSourceConfiguration;

    @Test
    void dataSourceShouldBeConfiguredUsingApplicationProperties() {
        PGSimpleDataSource expectedDataSource = new PGSimpleDataSource();
        expectedDataSource.setServerNames(new String[]{"test-server"});
        expectedDataSource.setDatabaseName("test-name");
        expectedDataSource.setPortNumbers(new int[]{5432});
        expectedDataSource.setUser("test-user");
        expectedDataSource.setPassword("test-pass");
        PGSimpleDataSource actualDataSource = (PGSimpleDataSource) dataSourceConfiguration.getPostgresDataSource();

        assertThat(actualDataSource).isEqualToComparingFieldByField(expectedDataSource);
    }
}
