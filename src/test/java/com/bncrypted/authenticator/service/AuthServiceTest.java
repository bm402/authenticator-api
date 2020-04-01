/*
package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceTest {

    private AuthService authService = new AuthServiceImpl();

    @Test
    void whenLeaseIsCalledWithValidCredentials_thenReturnToken() {
        UserAndOtp userAndOtp = new UserAndOtp("test-user", "test-password", "test-otp");
        Token expectedToken = new Token("token");
        Token actualToken = authService.lease(userAndOtp);

        assertThat(actualToken).isEqualToComparingFieldByField(expectedToken);
    }

    @Test
    void whenLeaseIsCalledForGuest_thenReturnGuestToken() {
        Token expectedToken = new Token("guest-token");
        Token actualToken = authService.leaseGuest();

        assertThat(actualToken).isEqualToComparingFieldByField(expectedToken);
    }

    @Test
    void whenVerifyIsCalledWithValidToken_thenReturnUsername() {
        Token token = new Token("test-token");
        UserResponse expectedUserResponse = new UserResponse("guest-user", "Verification successful");
        UserResponse actualUserResponse = authService.verify(token);

        assertThat(actualUserResponse).isEqualToComparingFieldByField(expectedUserResponse);
    }
}
*/
