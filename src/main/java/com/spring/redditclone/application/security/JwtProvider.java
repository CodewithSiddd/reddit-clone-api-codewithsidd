package com.spring.redditclone.application.security;

import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return generateJWTTokenWithUserName(principal.getUsername());
    }

    public String generateJWTTokenWithUserName(String userName) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtExpirationTime))
                .subject(userName)
                .claim("scope", "ROLE_USER")
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public Long getJwtExpirationTime() {
        return jwtExpirationTime;
    }

}
