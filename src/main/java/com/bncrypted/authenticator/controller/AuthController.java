package com.bncrypted.authenticator.controller;

import com.bncrypted.authenticator.model.TokenCredentials;
import com.bncrypted.authenticator.model.UserAndOtp;
import com.bncrypted.authenticator.model.UserTokenDetails;
import com.bncrypted.authenticator.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.bncrypted.authenticator.configuration.SwaggerConfiguration.SESSION_TAG;

@RestController
@Api(tags = {SESSION_TAG})
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/lease")
    @ApiOperation(value = "Lease session token using multi-factor authentication")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Token leased"),
            @ApiResponse(code = 404, message = "Invalid credentials")
    })
    public ResponseEntity<TokenCredentials> lease(@RequestBody @Valid UserAndOtp userAndOtp) {

        TokenCredentials tokenCredentials = authService.lease(userAndOtp);
        return ResponseEntity.ok(tokenCredentials);
    }

    @GetMapping(value = "/lease/guest")
    @ApiOperation(value = "Lease session token for guest")
    @ApiResponse(code = 200, message = "Token leased")
    public ResponseEntity<TokenCredentials> leaseGuest() {
        TokenCredentials tokenCredentials = authService.leaseGuest();
        return ResponseEntity.ok(tokenCredentials);
    }

    @PostMapping(value = "/verify")
    @ApiOperation(value = "Verify session token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Token verified"),
            @ApiResponse(code = 401, message = "Invalid token")
    })
    public ResponseEntity<UserTokenDetails> verify(@RequestBody @Valid TokenCredentials tokenCredentials) {
        UserTokenDetails userTokenDetails = authService.verify(tokenCredentials);
        return ResponseEntity.ok(userTokenDetails);
    }

}
