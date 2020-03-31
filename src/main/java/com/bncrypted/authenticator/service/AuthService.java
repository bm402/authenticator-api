package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserResponse;

public interface AuthService {

    Token lease(UserAndOtp userAndOtp);
    Token leaseGuest();
    UserResponse verify(Token token);

}
