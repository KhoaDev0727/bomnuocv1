package com.bomnuocv1.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginWithPinCommand {
    private final String phoneNumber;
    private final String pinCode;
}
