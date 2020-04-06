package com.bncrypted.authenticator.util.jwt;

import com.bncrypted.authenticator.exception.InvalidTokenException;
import com.bncrypted.authenticator.model.UserTokenDetails;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtHS512HelperTest {

    private static final UserTokenDetails TEST_USER_TOKEN_DETAILS =
            new UserTokenDetails("test-user", ImmutableSet.of("test-role"));

    private final JwtHelper<UserTokenDetails> jwtHelper = new JwtHS512Helper<>("YWFh", 3);

    @Test
    void shouldIssueTokenForUser() {
        assertNotNull(jwtHelper.issueTokenForSubject(TEST_USER_TOKEN_DETAILS));
    }

    @Test
    void shouldVerifyAndExtractUserTokenDetailsForValidToken() {
        String validToken = jwtHelper.issueTokenForSubject(TEST_USER_TOKEN_DETAILS);
        UserTokenDetails actualUserTokenDetails = jwtHelper.verifyAndExtractSubject(validToken, UserTokenDetails.class);

        assertThat(actualUserTokenDetails).isEqualToComparingFieldByField(TEST_USER_TOKEN_DETAILS);
    }

    @Test
    void shouldNotVerifyExpiredToken() throws InterruptedException {
        String expiredToken = jwtHelper.issueTokenForSubject(TEST_USER_TOKEN_DETAILS);
        TimeUnit.SECONDS.sleep(5);

        assertThrows(InvalidTokenException.class,
                () -> jwtHelper.verifyAndExtractSubject(expiredToken, UserTokenDetails.class));
    }

    @Test
    void shouldNotVerifyInvalidToken() {
        String validToken = jwtHelper.issueTokenForSubject(TEST_USER_TOKEN_DETAILS);
        String invalidToken = validToken + "invalid";

        assertThrows(InvalidTokenException.class,
                () -> jwtHelper.verifyAndExtractSubject(invalidToken, UserTokenDetails.class));
    }

}
