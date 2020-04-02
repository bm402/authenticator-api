package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.TokenCredentials;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserResponse;

public interface AuthService {

    TokenCredentials lease(UserAndOtp userAndOtp);
    TokenCredentials leaseGuest();
    UserResponse verify(TokenCredentials tokenCredentials);

}
