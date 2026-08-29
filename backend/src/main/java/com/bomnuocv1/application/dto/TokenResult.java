package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResult {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final long expiresInMs;
}
