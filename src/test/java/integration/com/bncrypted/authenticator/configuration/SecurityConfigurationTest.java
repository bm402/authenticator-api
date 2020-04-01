package integration.com.bncrypted.authenticator.configuration;

import com.bncrypted.authenticator.configuration.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfiguration.class)
public class SecurityConfigurationTest {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Test
    void passwordEncoderShouldBeConfigured() {
        PasswordEncoder passwordEncoder = securityConfiguration.getPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("test-password");

        assertTrue(passwordEncoder.matches("test-password", encodedPassword));
    }
}
