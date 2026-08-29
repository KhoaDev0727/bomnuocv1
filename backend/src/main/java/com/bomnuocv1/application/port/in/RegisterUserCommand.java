package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterUserCommand {
    private final String phoneNumber;
    private final String pinCode;
    private final String fullName;
    private final String roleCode;
    private final String otpCode;
    private final String firebaseIdToken;
}
