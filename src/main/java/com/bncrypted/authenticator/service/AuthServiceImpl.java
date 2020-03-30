package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.TokenVerification;
import com.bncrypted.authenticator.model.UserAndOtp;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    public Token lease(UserAndOtp userAndOtp) {
        return new Token("token");
    }

    public Token leaseGuest() {
        return new Token("guest-token");
    }

    public TokenVerification verify(Token token) {
        return new TokenVerification("verified");
    }

}
