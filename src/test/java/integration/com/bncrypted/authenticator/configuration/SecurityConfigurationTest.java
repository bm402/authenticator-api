package integration.com.bncrypted.authenticator.configuration;

import com.bncrypted.authenticator.configuration.SecurityConfiguration;
import com.bncrypted.authenticator.util.jwt.JwtHelper;
import com.bncrypted.authenticator.util.otp.OtpHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = SecurityConfiguration.class)
@TestPropertySource("classpath:test.properties")
public class SecurityConfigurationTest {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Test
    void passwordEncoderShouldBeConfigured() {
        PasswordEncoder passwordEncoder = securityConfiguration.getPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("test-password");

        assertTrue(passwordEncoder.matches("test-password", encodedPassword));
    }

    @Test
    void otpHelperShouldBeConfigured() {
        OtpHelper otpHelper = securityConfiguration.getOtpHelper();
        String otp = otpHelper.issueOtp("IFAUCQI=");

        assertTrue(otpHelper.isOtpValid("IFAUCQI=", otp));
    }

    @Test
    void jwtHelperShouldBeConfigured() {
        JwtHelper jwtHelper = securityConfiguration.getJwtHelper();
        String token = jwtHelper.issueTokenForUser("test-user");

        assertEquals("test-user", jwtHelper.verifyAndExtractUser(token));
    }
}
