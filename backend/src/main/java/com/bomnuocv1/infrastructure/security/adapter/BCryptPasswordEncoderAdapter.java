package com.bomnuocv1.infrastructure.security.adapter;

import com.bomnuocv1.application.port.out.PasswordEncoderPort;
import com.bomnuocv1.domain.valueobject.PinCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(PinCode pinCode) {
        return passwordEncoder.encode(pinCode.getValue());
    }

    @Override
    public boolean matches(PinCode pinCode, String encodedHash) {
        return passwordEncoder.matches(pinCode.getValue(), encodedHash);
    }
}
