package com.bncrypted.authenticator.util.jwt;

import com.bncrypted.authenticator.exception.InvalidTokenException;
import com.bncrypted.authenticator.model.TokenDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Clock;
import java.util.Date;
import java.util.Map;

public class JwtHS512Helper<T extends TokenDetails> implements JwtHelper<T> {

    private String signingKey;
    private int ttlInSeconds;
    private final Clock clock;

    public JwtHS512Helper() {
        this.clock = Clock.systemUTC();
    }

    public JwtHS512Helper(String signingKey, int ttlInSeconds) {
        this.signingKey = signingKey;
        this.ttlInSeconds = ttlInSeconds;
        this.clock = Clock.systemUTC();
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public void setTtlInSeconds(int ttlInSeconds) {
        this.ttlInSeconds = ttlInSeconds;
    }

    public String issueTokenForSubject(T tokenDetails) {
        return Jwts.builder()
                .setSubject(tokenDetails.getId())
                .setExpiration(Date.from(clock.instant().plusSeconds(ttlInSeconds)))
                .claim("details", tokenDetails)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    public T verifyAndExtractSubject(String token, Class<T> tokenDetailsClass) {
        try {
            Map details = Jwts.parser()
                    .setClock(() -> Date.from(clock.instant()))
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("details", Map.class);
            return new ObjectMapper().convertValue(details, tokenDetailsClass);
        } catch (JwtException ex) {
            throw new InvalidTokenException();
        }
    }

}
