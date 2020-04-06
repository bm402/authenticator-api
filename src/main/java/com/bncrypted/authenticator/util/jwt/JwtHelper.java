package com.bncrypted.authenticator.util.jwt;

public interface JwtHelper<T> {

    String issueTokenForSubject(T tokenDetails);
    T verifyAndExtractSubject(String token, Class<T> tokenDetailsClass);

}
