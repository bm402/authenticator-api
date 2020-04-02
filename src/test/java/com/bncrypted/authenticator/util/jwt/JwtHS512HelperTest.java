package com.bncrypted.authenticator.util.jwt;

import com.bncrypted.authenticator.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtHS512HelperTest {

    private static final String TEST_USER = "test-user";
    private final JwtHelper jwtHelper = new JwtHS512Helper("YWFh", 3);

    @Test
    void shouldIssueTokenForUser() {
        assertNotNull(jwtHelper.issueTokenForUser(TEST_USER));
    }

    @Test
    void shouldVerifyAndExtractUsernameForValidToken() {
        String validToken = jwtHelper.issueTokenForUser(TEST_USER);
        assertEquals(TEST_USER, jwtHelper.verifyAndExtractUser(validToken));
    }

    @Test
    void shouldNotVerifyExpiredToken() throws InterruptedException {
        String expiredToken = jwtHelper.issueTokenForUser(TEST_USER);
        TimeUnit.SECONDS.sleep(5);
        assertThrows(InvalidTokenException.class, () -> jwtHelper.verifyAndExtractUser(expiredToken));
    }

    @Test
    void shouldNotVerifyInvalidToken() {
        String validToken = jwtHelper.issueTokenForUser(TEST_USER);
        String invalidToken = validToken + "invalid";
        assertThrows(InvalidTokenException.class, () -> jwtHelper.verifyAndExtractUser(invalidToken));
    }

}
