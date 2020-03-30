package com.bncrypted.authenticator.exception;

public class InvalidCredentialsException extends AuthenticatorException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
