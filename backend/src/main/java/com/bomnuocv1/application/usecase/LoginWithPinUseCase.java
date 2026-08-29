package com.bomnuocv1.application.usecase;

import com.bomnuocv1.application.dto.AuthResult;
import com.bomnuocv1.application.port.in.LoginWithPinCommand;

public interface LoginWithPinUseCase {
    AuthResult execute(LoginWithPinCommand command);
}
