package integration.com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.exception.InvalidCredentialsException;
import com.bncrypted.authenticator.exception.InvalidTokenException;
import com.bncrypted.authenticator.model.TokenCredentials;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.model.UserTokenDetails;
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
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthServiceTest extends IntegrationBaseTest {

    private final OtpHelper otpHelper = new TotpHelper();
    private final JwtHelper<UserTokenDetails> jwtHelper = new JwtHS512Helper<>("YWFh", 20);
    private final AuthService authService = new AuthServiceImpl(dataSource, passwordEncoder, otpHelper, jwtHelper);

    private String username;
    private String existingPassword;
    private String existingMfaKey;
    private String existingRole;

    @BeforeEach
    void setup() {
        username = UUID.randomUUID().toString();
        existingPassword = UUID.randomUUID().toString();
        existingMfaKey = BaseEncoding.base32().encode(UUID.randomUUID().toString().getBytes());
        existingRole = UUID.randomUUID().toString();
        UserCredentials existingUserCredentials = new UserCredentials(username,
                passwordEncoder.encode(existingPassword), existingMfaKey);

        databaseHelper.addUser(existingUserCredentials);
        databaseHelper.addUserRole(username, existingRole);
    }

    @Test
    void whenLeasingTokenWithValidCredentials_thenValidTokenShouldBeReturned() {
        String validOtp = otpHelper.issueOtp(existingMfaKey);
        UserAndOtp validCredentials = new UserAndOtp(username, existingPassword, validOtp);
        TokenCredentials actualTokenCredentials = authService.lease(validCredentials);

        UserTokenDetails expectedUserTokenDetails = new UserTokenDetails(username, ImmutableSet.of(existingRole));
        UserTokenDetails actualUserTokenDetails = jwtHelper.verifyAndExtractSubject(
                actualTokenCredentials.getToken(), UserTokenDetails.class);

        assertThat(actualUserTokenDetails).isEqualToComparingFieldByField(expectedUserTokenDetails);
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
    void whenLeasingTokenAsGuest_thenValidGuestTokenShouldBeReturned() {
        TokenCredentials actualTokenCredentials = authService.leaseGuest();

        UserTokenDetails expectedUserTokenDetails = new UserTokenDetails("guest", ImmutableSet.of("guest"));
        UserTokenDetails actualUserTokenDetails = jwtHelper.verifyAndExtractSubject(
                actualTokenCredentials.getToken(), UserTokenDetails.class);

        assertThat(actualUserTokenDetails).isEqualToComparingFieldByField(expectedUserTokenDetails);
    }

    @Test
    void whenVerifyingValidToken_thenUserTokenDetailsShouldBeReturned() {
        String validOtp = otpHelper.issueOtp(existingMfaKey);
        UserAndOtp validCredentials = new UserAndOtp(username, existingPassword, validOtp);
        TokenCredentials validTokenCredentials = authService.lease(validCredentials);

        UserTokenDetails expectedUserTokenDetails = new UserTokenDetails(username, ImmutableSet.of(existingRole));
        UserTokenDetails actualUserTokenDetails = authService.verify(validTokenCredentials);

        assertThat(actualUserTokenDetails).isEqualToComparingFieldByField(expectedUserTokenDetails);
    }

    @Test
    void whenVerifyingMalformedToken_thenUserTokenDetailsShouldNotBeReturned() {
        TokenCredentials invalidTokenCredentials = new TokenCredentials("malformed-token");

        InvalidTokenException actualException = assertThrows(InvalidTokenException.class,
                () -> authService.verify(invalidTokenCredentials));

        assertEquals("Invalid token", actualException.getMessage());
    }

    @Test
    void whenVerifyingInvalidToken_thenUserTokenDetailsShouldNotBeReturned() {
        String validOtp = otpHelper.issueOtp(existingMfaKey);
        UserAndOtp validCredentials = new UserAndOtp(username, existingPassword, validOtp);
        TokenCredentials validTokenCredentials = authService.lease(validCredentials);
        TokenCredentials invalidTokenCredentials = new TokenCredentials(validTokenCredentials.getToken() + "invalid");

        InvalidTokenException actualException = assertThrows(InvalidTokenException.class,
                () -> authService.verify(invalidTokenCredentials));

        assertEquals("Invalid token", actualException.getMessage());
    }

}
