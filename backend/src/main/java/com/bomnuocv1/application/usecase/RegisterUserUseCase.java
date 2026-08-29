package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.port.in.RegisterUserCommand;

public interface RegisterUserUseCase {
    AuthResult execute(RegisterUserCommand command);
}
