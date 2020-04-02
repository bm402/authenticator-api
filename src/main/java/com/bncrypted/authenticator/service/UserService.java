package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;

public interface UserService {

    UserResponse addUser(UserAndPassword userAndPassword);
    UserResponse updateUserPassword(UserAndNewPassword userAndNewPassword);
    UserResponse updateUserMfaKey(UserAndPassword userAndPassword);
    UserResponse deleteUser(UserAndPassword userAndPassword);

}
