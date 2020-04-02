package com.bncrypted.authenticator.util.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Clock;
import java.util.Date;

public class JwtHS512Helper implements JwtHelper {

    private String key;
    private int ttlInSeconds;
    private final Clock clock;

    public JwtHS512Helper() {
        this.clock = Clock.systemUTC();
    }

    public JwtHS512Helper(String key, int ttlInSeconds) {
        this.key = key;
        this.ttlInSeconds = ttlInSeconds;
        this.clock = Clock.systemUTC();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTtlInSeconds(int ttlInSeconds) {
        this.ttlInSeconds = ttlInSeconds;
    }

    public String issueTokenForUser(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(clock.instant().plusSeconds(ttlInSeconds)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String verifyAndExtractUser(String jwt) {
        return Jwts.parser()
                .setClock(() -> Date.from(clock.instant()))
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

}
