package com.reddit.RedditClone.service;

import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.reddit.RedditClone.exception.SpringRedditException;
import com.reddit.RedditClone.models.NotificationEmail;
import com.reddit.RedditClone.models.User;
import com.reddit.RedditClone.models.VerificationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import com.reddit.RedditClone.dto.AuthenticationResponse;
import com.reddit.RedditClone.dto.LoginRequest;
import com.reddit.RedditClone.dto.RefreshTokenRequest;
import com.reddit.RedditClone.dto.RegisterRequest;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.repository.VerificationTokenRepository;
import com.reddit.RedditClone.secruity.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {
    private final PasswordEncoder passwordEncoder;//BCrypt Encoding
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String vToken = genrateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate Your Account",
                user.getEmail(),"Thank you for Signing up with my fake Redit,"
                + "Please click in the beliw link to activate your account,"
                + "http://localhost:8080/api/auth/accountVerification/"+vToken));
    }
    private String genrateVerificationToken(User user) {
        // TODO Auto-generated method stub
        String vToken = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(vToken);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return vToken;
    }
    public void verifyAccount(String vToken) {
        // TODO Auto-generated method stub
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(vToken);
        GeneralSecurityException e = null;
        verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token", e));
        EnableUser(verificationToken.get());
    }

    @Transactional
    private void EnableUser(VerificationToken verificationToken) {
        // TODO Auto-generated method stub
        String username = verificationToken.getUser().getUsername();
        GeneralSecurityException e =null;
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User Not Found : " + username, e));
        user.setEnabled(true);
        userRepository.save(user);
    }
    public AuthenticationResponse login(LoginRequest loginrequest) {
        // TODO Auto-generated method stub
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginrequest.getUsername(),loginrequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.genarateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginrequest.getUsername())
                .build();
    }
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication  principal =  SecurityContextHolder.
                getContext().getAuthentication();
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getName()));
    }
    public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}