package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.Token;
import com.bncrypted.authenticator.model.TokenVerification;
import com.bncrypted.authenticator.model.UserCredentials;
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
    public ResponseEntity<Token> lease(@RequestBody @Valid UserCredentials userCredentials) {
        Token token = authService.lease(userCredentials);
        return ResponseEntity.ok(token);
    }

    @GetMapping(value = "/lease/guest")
    public ResponseEntity<Token> leaseGuest() {
        Token token = authService.leaseGuest();
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<TokenVerification> verify(@RequestBody @Valid Token token) {
        TokenVerification tokenVerification = authService.verify(token);
        return ResponseEntity.ok(tokenVerification);
    }

}
