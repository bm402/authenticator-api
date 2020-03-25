package com.bncrypted.authenticator.service.impl;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.TokenVerification;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    public Token lease(UserCredentials userCredentials) {
        return new Token("token");
    }

    public Token leaseGuest(UserCredentials userCredentials) {
        return new Token("guest");
    }

    public TokenVerification verify(Token token) {
        return new TokenVerification("verified");
    }

}
