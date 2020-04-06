package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.TokenCredentials;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserTokenDetails;
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
    public ResponseEntity<TokenCredentials> lease(@RequestBody @Valid UserAndOtp userAndOtp) {
        TokenCredentials tokenCredentials = authService.lease(userAndOtp);
        return ResponseEntity.ok(tokenCredentials);
    }

    @GetMapping(value = "/lease/guest")
    public ResponseEntity<TokenCredentials> leaseGuest() {
        TokenCredentials tokenCredentials = authService.leaseGuest();
        return ResponseEntity.ok(tokenCredentials);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<UserTokenDetails> verify(@RequestBody @Valid TokenCredentials tokenCredentials) {
        UserTokenDetails userTokenDetails = authService.verify(tokenCredentials);
        return ResponseEntity.ok(userTokenDetails);
    }

}
