package com.bncrypted.authenticator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserAndPassword {

    @NotNull
    private String username;

    @NotNull
    private String password;

}
