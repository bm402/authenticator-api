package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.UserAndMfaKeyResponse;
import com.bncrypted.authenticator.model.UserAndNewPassword;
import com.bncrypted.authenticator.model.UserAndPassword;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.bncrypted.authenticator.configuration.SwaggerConfiguration.USER_TAG;

@RestController
@RequestMapping("/users")
@Api(tags = {USER_TAG})
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation(value = "Add new user profile and generate new multi-factor authentication key")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User profile created"),
            @ApiResponse(code = 404, message = "Username already taken")
    })
    public ResponseEntity<UserAndMfaKeyResponse> addUser(@RequestBody @Valid UserAndPassword userAndPassword) {
        UserAndMfaKeyResponse userAndMfaKeyResponse = userService.addUser(userAndPassword);
        return new ResponseEntity<>(userAndMfaKeyResponse, HttpStatus.CREATED);
    }

    @PutMapping("/password")
    @ApiOperation(value = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User password updated"),
            @ApiResponse(code = 404, message = "Invalid credentials")
    })
    public ResponseEntity<UserResponse> updateUserPassword(@RequestBody @Valid UserAndNewPassword userAndNewPassword) {
        UserResponse userResponse = userService.updateUserPassword(userAndNewPassword);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/mfa-key")
    @ApiOperation(value = "Generate new multi-factor authentication key for user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User MFA key updated"),
            @ApiResponse(code = 404, message = "Invalid credentials")
    })
    public ResponseEntity<UserAndMfaKeyResponse> updateUserMfaKey(@RequestBody @Valid UserAndPassword userAndPassword) {
        UserAndMfaKeyResponse userAndMfaKeyResponse = userService.updateUserMfaKey(userAndPassword);
        return new ResponseEntity<>(userAndMfaKeyResponse, HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation(value = "Delete user profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User profile deleted"),
            @ApiResponse(code = 404, message = "Invalid credentials")
    })
    public ResponseEntity<UserResponse> deleteUser(@RequestBody @Valid UserAndPassword userAndPassword) {
        UserResponse userResponse = userService.deleteUser(userAndPassword);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
