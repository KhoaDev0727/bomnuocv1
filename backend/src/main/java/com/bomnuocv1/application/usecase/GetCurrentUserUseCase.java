package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.UserResult;

import java.util.UUID;

public interface GetCurrentUserUseCase {
    UserResult execute(UUID userId);
}
