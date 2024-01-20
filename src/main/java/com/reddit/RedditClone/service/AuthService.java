package com.reddit.RedditClone.service;

import com.reddit.RedditClone.dto.AuthenticationResponse;
import com.reddit.RedditClone.dto.LoginRequest;
import com.reddit.RedditClone.dto.RefreshTokenRequest;
import com.reddit.RedditClone.dto.RegisterRequest;
import com.reddit.RedditClone.exception.SpringRedditException;
import com.reddit.RedditClone.models.NotificationEmail;
import com.reddit.RedditClone.models.User;
import com.reddit.RedditClone.models.VerificationToken;
import com.reddit.RedditClone.repository.UserRepository;
import com.reddit.RedditClone.repository.VerificationTokenRepository;
import com.reddit.RedditClone.secruity.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.reddit.RedditClone.util.Contants.ACTIVATION_EMAIL;
//Videos 4 is the most important one.
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
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Activate Account",user.getEmail(),"Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
                + ACTIVATION_EMAIL + "/" + token));
    }

    private String generateVerificationToken(User user) {
        String Token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(Token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return Token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("you have a invalid token"));
        fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new SpringRedditException("User Not founf " + userName));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
