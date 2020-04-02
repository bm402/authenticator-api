package com.bncrypted.authenticator.util.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
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
        String token = jwtHelper.issueTokenForUser(TEST_USER);
        assertEquals(TEST_USER, jwtHelper.verifyAndExtractUser(token));
    }

    @Test
    void shouldNotVerifyExpiredToken() throws InterruptedException {
        String token = jwtHelper.issueTokenForUser(TEST_USER);
        TimeUnit.SECONDS.sleep(5);
        assertThrows(ExpiredJwtException.class, () -> jwtHelper.verifyAndExtractUser(token));
    }

    @Test
    void shouldNotVerifyInvalidToken() {
        String validToken = jwtHelper.issueTokenForUser(TEST_USER);
        String invalidToken = validToken + "invalid";
        assertThrows(SignatureException.class, () -> jwtHelper.verifyAndExtractUser(invalidToken));
    }

}
