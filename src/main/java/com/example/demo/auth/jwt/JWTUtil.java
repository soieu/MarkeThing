package com.example.demo.auth.jwt;

import com.example.demo.auth.service.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private final SecretKey secretKey;
    private final CustomUserDetailsService customUserDetailsService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret,
            CustomUserDetailsService customUserDetailsService) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());

        this.customUserDetailsService = customUserDetailsService;
    }

    public String getUserEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String createJwt(String userEmail, Long expireMs) {
        return Jwts.builder()
                .claim("username", userEmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String authToken) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserEmail(authToken));
//        UserDetails userDetails = siteUserRepository.findByEmail(this.getUserEmail(authToken))
//                .orElseThrow(() -> new MarkethingException(ErrorCode.USER_NOT_FOUND));
        return new UsernamePasswordAuthenticationToken(userDetails, authToken, userDetails.getAuthorities());
    }

}
