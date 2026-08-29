package com.bomnuocv1.application.port.out;

import com.bomnuocv1.domain.valueobject.PinCode;

public interface PasswordEncoderPort {

    String encode(PinCode pinCode);

    boolean matches(PinCode pinCode, String encodedHash);
}
