package integration.com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.UserAndHashedPassword;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.service.AuthService;
import com.bncrypted.authenticator.service.AuthServiceImpl;
import com.bncrypted.authenticator.util.jwt.JwtHS512Helper;
import com.bncrypted.authenticator.util.jwt.JwtHelper;
import com.bncrypted.authenticator.util.otp.OtpHelper;
import com.bncrypted.authenticator.util.otp.TotpHelper;
import com.google.common.io.BaseEncoding;
import integration.com.bncrypted.authenticator.base.IntegrationBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthServiceTest extends IntegrationBaseTest {

    private final OtpHelper otpHelper = new TotpHelper();
    private final JwtHelper jwtHelper = new JwtHS512Helper("YWFh", 20);
    private final AuthService authService = new AuthServiceImpl(dataSource, passwordEncoder, otpHelper, jwtHelper);

    private String username;
    private String existingPassword;
    private String existingMfaKey;

    @BeforeEach
    void setup() {
        username = UUID.randomUUID().toString();
        existingPassword = UUID.randomUUID().toString();
        existingMfaKey = BaseEncoding.base32().encode(UUID.randomUUID().toString().getBytes());
        UserCredentials existingUserCredentials = new UserCredentials(username,
                passwordEncoder.encode(existingPassword), existingMfaKey);
        databaseHelper.addUser(existingUserCredentials);
    }

    @Test
    void whenLeasingTokenWithValidCredentials_thenValidTokenShouldBeReturned() {
        String validOtp = otpHelper.issueOtp(existingMfaKey);
        UserAndOtp validCredentials = new UserAndOtp(username, existingPassword, validOtp);
        Token actualToken = authService.lease(validCredentials);

        assertEquals(username, jwtHelper.verifyAndExtractUser(actualToken.getToken()));
    }

    @Test
    void whenLeasingTokenWithInvalidPassword_thenTokenShouldNotBeProvided() {
        String validOtp = otpHelper.issueOtp(existingMfaKey);
        UserAndOtp invalidCredentials = new UserAndOtp(username, UUID.randomUUID().toString(), validOtp);

        InvalidCredentialsException actualException = assertThrows(InvalidCredentialsException.class,
                () -> authService.lease(invalidCredentials));

        assertEquals("Invalid credentials", actualException.getMessage());
    }

    @Test
    void whenLeasingTokenWithInvalidOtp_thenTokenShouldNotBeProvided() {
        String invalidOtp = String.valueOf(Integer.parseInt(otpHelper.issueOtp(existingMfaKey)) + 1 % 1000000);
        UserAndOtp invalidCredentials = new UserAndOtp(username, existingPassword, invalidOtp);

        InvalidCredentialsException actualException = assertThrows(InvalidCredentialsException.class,
                () -> authService.lease(invalidCredentials));

        assertEquals("Invalid one-time password", actualException.getMessage());
    }

    @Test
    void whenLeasingTokenAsGuest_thenGuestTokenShouldBeReturned() {
        Token actualToken = authService.leaseGuest();

        assertEquals("guest", jwtHelper.verifyAndExtractUser(actualToken.getToken()));
    }
}
