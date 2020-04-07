package com.bncrypted.authenticator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAndMfaKeyResponse {

    private String username;
    private String mfaKey;
    private String message;

}
