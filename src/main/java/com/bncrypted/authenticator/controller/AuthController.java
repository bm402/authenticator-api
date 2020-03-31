package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserResponse;
import com.bncrypted.authenticator.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/lease")
    public ResponseEntity<Token> lease(@RequestBody @Valid UserAndOtp userAndOtp) {
        Token token = authService.lease(userAndOtp);
        return ResponseEntity.ok(token);
    }

    @GetMapping(value = "/lease/guest")
    public ResponseEntity<Token> leaseGuest() {
        Token token = authService.leaseGuest();
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<UserResponse> verify(@RequestBody @Valid Token token) {
        UserResponse userResponse = authService.verify(token);
        return ResponseEntity.ok(userResponse);
    }

}
