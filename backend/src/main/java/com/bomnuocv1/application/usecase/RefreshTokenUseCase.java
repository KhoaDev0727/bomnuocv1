package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.TokenResult;
import com.bomnuocv1.application.port.in.RefreshTokenCommand;

public interface RefreshTokenUseCase {
    TokenResult execute(RefreshTokenCommand command);
}
