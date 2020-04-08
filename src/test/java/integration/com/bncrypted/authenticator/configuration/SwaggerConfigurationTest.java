package integration.com.bncrypted.authenticator.configuration;

import com.bncrypted.authenticator.configuration.SwaggerConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SwaggerConfiguration.class)
public class SwaggerConfigurationTest {

    @Autowired
    private SwaggerConfiguration swaggerConfiguration;

    @Test
    void swaggerShouldBeConfigured() {
        Docket docket = swaggerConfiguration.getApiDocs();
        assertNotNull(docket);
    }

}
