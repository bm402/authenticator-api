package com.bncrypted.authenticator.exception;

public class InvalidTokenException extends AuthenticatorException {

    public InvalidTokenException() {
        super("Invalid token");
    }

}
