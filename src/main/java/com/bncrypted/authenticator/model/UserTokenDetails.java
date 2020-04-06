package com.bncrypted.authenticator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserTokenDetails extends TokenDetails {

    private Set<String> roles;

    public UserTokenDetails(String id, Set<String> roles) {
        super(id);
        this.roles = roles;
    }

}
