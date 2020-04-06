package com.bncrypted.authenticator.model;

import lombok.Getter;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

@Getter
public class UserCredentials {

    private int id;
    private String username;
    private String hashedPassword;
    private String mfaKey;

    public UserCredentials(String username, String hashedPassword, String mfaKey) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.mfaKey = mfaKey;
    }

    @JdbiConstructor
    public UserCredentials(int id, String username, String hashedPassword, String mfaKey) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.mfaKey = mfaKey;
    }

}
