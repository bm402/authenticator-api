package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.TokenVerification;
import com.bncrypted.authenticator.model.UserCredentials;

public interface AuthService {

    Token lease(UserCredentials userCredentials);
    Token leaseGuest();
    TokenVerification verify(Token token);

}
