package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.UserAndMfaKeyResponse;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;

public interface UserService {

    UserAndMfaKeyResponse addUser(UserAndPassword userAndPassword);
    UserResponse updateUserPassword(UserAndNewPassword userAndNewPassword);
    UserAndMfaKeyResponse updateUserMfaKey(UserAndPassword userAndPassword);
    UserResponse deleteUser(UserAndPassword userAndPassword);

}
