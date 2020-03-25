package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.TokenVerification;
import com.bncrypted.authenticator.model.UserCredentials;
import com.bncrypted.authenticator.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/lease")
    public ResponseEntity<Token> lease(@RequestBody UserCredentials userCredentials) {
        Token token = authService.lease(userCredentials);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/lease/guest")
    public ResponseEntity<Token> leaseGuest(@RequestBody UserCredentials userCredentials) {
        Token token = authService.leaseGuest(userCredentials);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<TokenVerification> verify(@RequestBody Token token) {
        TokenVerification tokenVerification = authService.verify(token);
        return ResponseEntity.ok(tokenVerification);
    }

}
