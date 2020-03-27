package com.bncrypted.authenticator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UserAndOtp {

    @NotNull
    private String username;

    @NotNull
    private String oneTimePassword;

}
