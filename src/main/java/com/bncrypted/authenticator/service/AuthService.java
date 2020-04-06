package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.TokenCredentials;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserTokenDetails;

public interface AuthService {

    TokenCredentials lease(UserAndOtp userAndOtp);
    TokenCredentials leaseGuest();
    UserTokenDetails verify(TokenCredentials tokenCredentials);

}
