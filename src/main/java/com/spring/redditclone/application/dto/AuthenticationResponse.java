package com.spring.redditclone.application.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String authenticationToken;

    private String userName;

    private String refreshToken;

    private Instant expiresAt;
}
