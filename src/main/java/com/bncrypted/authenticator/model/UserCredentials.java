package com.bncrypted.authenticator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentials {

    private String username;
    private String oneTimePassword;

}
