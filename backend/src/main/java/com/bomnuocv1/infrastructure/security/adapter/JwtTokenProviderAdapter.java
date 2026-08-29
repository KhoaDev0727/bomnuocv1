package com.bomnuocv1.infrastructure.security.adapter;

import com.bomnuocv1.application.port.out.TokenProviderPort;
import com.bomnuocv1.domain.entity.User;
import com.bomnuocv1.domain.valueobject.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProviderAdapter(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${app.jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs
    ) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Override
    public TokenPair generateTokenPair(User user) {
        Date now = new Date();
        Date accessExpiry = new Date(now.getTime() + accessTokenExpirationMs);
        Date refreshExpiry = new Date(now.getTime() + refreshTokenExpirationMs);

        String accessToken = Jwts.builder()
                .subject(user.getId().toString())
                .claim("phone", user.getPhoneNumber().getValue())
                .claim("role", user.getRole() != null ? user.getRole().getCode() : "owner")
                .claim("name", user.getFullName())
                .claim("token_type", "access")
                .issuedAt(now)
                .expiration(accessExpiry)
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(user.getId().toString())
                .claim("token_type", "refresh")
                .issuedAt(now)
                .expiration(refreshExpiry)
                .signWith(secretKey)
                .compact();

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInMs(accessTokenExpirationMs)
                .build();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateTokenWithExpectedType(token, "access");
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateTokenWithExpectedType(token, "refresh");
    }

    private boolean validateTokenWithExpectedType(String token, String expectedType) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenType = claims.get("token_type", String.class);
            return expectedType.equalsIgnoreCase(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public UUID extractUserIdFromAccessToken(String token) {
        return extractUserId(token);
    }

    @Override
    public UUID extractUserIdFromRefreshToken(String token) {
        return extractUserId(token);
    }

    private UUID extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    @Override
    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }
}
