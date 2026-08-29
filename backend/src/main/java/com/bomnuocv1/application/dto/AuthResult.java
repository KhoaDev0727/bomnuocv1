package com.bomnuocv1.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResult {
    private final UserResult user;
    private final TokenResult tokens;
}
