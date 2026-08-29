package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenCommand {
    private final String refreshToken;
}
