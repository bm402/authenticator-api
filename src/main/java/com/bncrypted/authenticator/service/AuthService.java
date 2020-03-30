package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.TokenVerification;
import com.bncrypted.authenticator.model.UserAndOtp;

public interface AuthService {

    Token lease(UserAndOtp userAndOtp);
    Token leaseGuest();
    TokenVerification verify(Token token);

}
