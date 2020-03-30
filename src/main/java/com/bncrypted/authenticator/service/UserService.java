package com.bncrypted.authenticator.service;

import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;

public interface UserService {

    UserResponse addUser(UserAndPassword userAndPassword);
    UserResponse updateUser(UserAndNewPassword userAndNewPassword);
    UserResponse deleteUser(UserAndPassword userAndPassword);

}
