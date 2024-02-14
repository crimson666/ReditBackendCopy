package com.reddit.RedditClone.secruity;

import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import static java.util.Date.from;

import javax.annotation.PostConstruct;

import com.reddit.RedditClone.exception.SpringRedditException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import static io.jsonwebtoken.Jwts.parser;
@Service
public class JwtProvider {
    private KeyStore keyStore;
    @Getter
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream,"secret".toCharArray());
        }catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            assert e instanceof GeneralSecurityException;
            throw new SpringRedditException("Exception Occard while loading Keystore", (GeneralSecurityException) e);
        }
    }

    public String genarateToken(Authentication authentication) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();//cating to srpring sectruity userdetails.User Obj
        return Jwts.builder().setSubject(loggedInUser.getName()).signWith(getPrivateKey()).setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis))).compact();
    }

    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        }catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new SpringRedditException("Exception Occard while fetching privatekey Keystore",e);
        }
    }

    //Validating the token with public key
    public boolean validateToken(String Jwt) {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(Jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        }catch(KeyStoreException e){
            throw new SpringRedditException("Exception Occard while fetching PublicKey Keystore",e);
        }
    }
    public String getUserNameFromToken(String token) {
        Claims claims = parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}