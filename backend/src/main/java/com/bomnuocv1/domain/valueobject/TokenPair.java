package com.bomnuocv1.domain.valueobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@EqualsAndHashCode
public class TokenPair implements Serializable {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final long expiresInMs;

    public TokenPair(String accessToken, String refreshToken, String tokenType, long expiresInMs) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.expiresInMs = expiresInMs;
    }
}
