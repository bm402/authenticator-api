package integration.com.bncrypted.authenticator.configuration;

import com.bncrypted.authenticator.configuration.SecurityConfiguration;
import com.bncrypted.authenticator.model.UserTokenDetails;
import com.bncrypted.authenticator.util.jwt.JwtHelper;
import com.bncrypted.authenticator.util.otp.OtpHelper;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
        JwtHelper<UserTokenDetails> jwtHelper = securityConfiguration.getJwtHelper();
        UserTokenDetails expectedUserTokenDetails = new UserTokenDetails("test-user", ImmutableSet.of("test-role"));
        String token = jwtHelper.issueTokenForSubject(expectedUserTokenDetails);
        UserTokenDetails actualUserTokenDetails = jwtHelper.verifyAndExtractSubject(token, UserTokenDetails.class);

        assertThat(actualUserTokenDetails).isEqualToComparingFieldByField(expectedUserTokenDetails);
    }
}
