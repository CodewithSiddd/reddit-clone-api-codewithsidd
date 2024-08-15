package com.spring.redditclone.application.controller;

import com.spring.redditclone.application.Exception.RedditCloneException;
import com.spring.redditclone.application.dto.AuthenticationResponse;
import com.spring.redditclone.application.dto.LoginRequest;
import com.spring.redditclone.application.dto.RefreshTokenRequest;
import com.spring.redditclone.application.dto.RegisterRequest;
import com.spring.redditclone.application.model.VerficationToken;
import com.spring.redditclone.application.service.AuthService;
import com.spring.redditclone.application.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws RedditCloneException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Succesfully Registered", HttpStatus.OK);
    }

    @GetMapping("accVerification/{tokenId}")
    public ResponseEntity<String> verifyYourAccount(@PathVariable String tokenId) throws RedditCloneException {
        authService.verifyYourAccount(tokenId);
        return new ResponseEntity<>("Account Activation is Successful!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh token Deleted successfully.");
    }

}
