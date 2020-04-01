package com.bncrypted.authenticator.util.jwt;

public interface JwtHelper {

    String issueTokenForUser(String username);
    String verifyAndExtractUser(String jwt);

}
