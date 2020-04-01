package com.bncrypted.authenticator.util.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Clock;
import java.util.Date;

public class JwtHS512Helper implements JwtHelper {

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private final String jwtKey;
    private final int ttlInSeconds;
    private final Clock clock;

    public JwtHS512Helper(String jwtKey, int ttlInSeconds) {
        this.jwtKey = jwtKey;
        this.ttlInSeconds = ttlInSeconds;
        this.clock = Clock.systemUTC();
    }

    public String issueTokenForUser(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(Date.from(clock.instant().plusSeconds(ttlInSeconds)))
                .signWith(SIGNATURE_ALGORITHM, jwtKey)
                .compact();
    }

    public String verifyAndExtractUser(String jwt) {
        return Jwts.parser()
                .setClock(() -> Date.from(clock.instant()))
                .setSigningKey(jwtKey)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

}
