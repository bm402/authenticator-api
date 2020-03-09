package com.bncrypted.authenticator.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Welcome to the Authenticator API");
    }

}
