package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.UserAndMfaKeyResponse;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserAndMfaKeyResponse> addUser(@RequestBody @Valid UserAndPassword userAndPassword) {
        UserAndMfaKeyResponse userAndMfaKeyResponse = userService.addUser(userAndPassword);
        return new ResponseEntity<>(userAndMfaKeyResponse, HttpStatus.CREATED);
    }

    @PutMapping("/password")
    public ResponseEntity<UserResponse> updateUserPassword(@RequestBody @Valid UserAndNewPassword userAndNewPassword) {
        UserResponse userResponse = userService.updateUserPassword(userAndNewPassword);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/mfa-key")
    public ResponseEntity<UserAndMfaKeyResponse> updateUser(@RequestBody @Valid UserAndPassword userAndPassword) {
        UserAndMfaKeyResponse userAndMfaKeyResponse = userService.updateUserMfaKey(userAndPassword);
        return new ResponseEntity<>(userAndMfaKeyResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<UserResponse> deleteUser(@RequestBody @Valid UserAndPassword userAndPassword) {
        UserResponse userResponse = userService.deleteUser(userAndPassword);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
