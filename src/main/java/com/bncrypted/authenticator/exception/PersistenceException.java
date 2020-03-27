package com.bncrypted.authenticator.exception;

public class PersistenceException extends AuthenticatorException {

    public PersistenceException() {
        super("Invalid credentials");
    }

    public PersistenceException(String message) {
        super(message);
    }

}
