package com.bncrypted.authenticator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping(value = "/")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to the Authenticator API");
    }

}
