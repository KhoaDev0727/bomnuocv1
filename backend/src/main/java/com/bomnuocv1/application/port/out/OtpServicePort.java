package com.bomnuocv1.application.port.out;

import com.bomnuocv1.domain.valueobject.PhoneNumber;

public interface OtpServicePort {

    boolean verifyFirebaseIdToken(String firebaseIdToken, PhoneNumber expectedPhone);
}
