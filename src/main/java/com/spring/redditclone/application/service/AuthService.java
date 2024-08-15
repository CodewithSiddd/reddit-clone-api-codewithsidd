package com.spring.redditclone.application.service;

import com.spring.redditclone.application.Exception.RedditCloneException;
import com.spring.redditclone.application.dto.AuthenticationResponse;
import com.spring.redditclone.application.dto.LoginRequest;
import com.spring.redditclone.application.dto.RefreshTokenRequest;
import com.spring.redditclone.application.dto.RegisterRequest;
import com.spring.redditclone.application.model.NotificationEmail;
import com.spring.redditclone.application.model.User;
import com.spring.redditclone.application.model.VerficationToken;
import com.spring.redditclone.application.repository.UserRepository;
import com.spring.redditclone.application.repository.VerificationTokenRepository;
import com.spring.redditclone.application.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws RedditCloneException {
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        //Saving a new user/customer/client
        userRepository.save(user);

        String tokenId = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account!",
                user.getEmail(), "Thank you for signing up with us. Please " +
                "click on below link to activate your account : " +
                "http://localhost:8080/api/auth/accVerification/" + tokenId));

    }

    private String generateVerificationToken(User user) {
        String tokenId = UUID.randomUUID().toString();
        VerficationToken verificationToken = new VerficationToken();
        verificationToken.setToken(tokenId);
        verificationToken.setUser(user);

        //Saving verification token id
        verificationTokenRepository.save(verificationToken);
        return tokenId;
    }

    public void verifyYourAccount(String tokenId) throws RedditCloneException {
        Optional<VerficationToken> verificationToken = verificationTokenRepository.findByToken(tokenId);
        verificationToken.orElseThrow(() -> new RedditCloneException("Invalid Token Id"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerficationToken verficationToken) throws RedditCloneException {
        String userName = verficationToken.getUser().getUserName();
        User userObj= userRepository.findByUserName(userName).orElseThrow(() -> new RedditCloneException("User does not exists with name - " + userName));
        userObj.setEnabled(true);
        userRepository.save(userObj);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTime()))
                .userName(loginRequest.getUserName())
                .build();
    }

    public User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUserName(jwt.getSubject()).orElseThrow(() -> new UsernameNotFoundException("User name not found."));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateJWTTokenWithUserName(refreshTokenRequest.getUserName());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTime()))
                .userName(refreshTokenRequest.getUserName())
                .build();
    }

}
