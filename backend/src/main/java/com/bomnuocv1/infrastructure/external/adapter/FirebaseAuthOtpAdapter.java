package com.bomnuocv1.infrastructure.external.adapter;

import com.bomnuocv1.application.port.out.OtpServicePort;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirebaseAuthOtpAdapter implements OtpServicePort {

    @Value("${firebase.auth.mock-enabled:true}")
    private boolean mockEnabled;

    @Override
    public boolean verifyFirebaseIdToken(String firebaseIdToken, PhoneNumber expectedPhone) {
        if (firebaseIdToken == null || firebaseIdToken.trim().isEmpty()) {
            return false;
        }

        // Mock verification for development / test
        if (mockEnabled || FirebaseApp.getApps().isEmpty()) {
            log.info("[MOCK FIREBASE AUTH] Verifying token for phone: {}", expectedPhone.getValue());
            return true;
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String phoneInToken = (String) decodedToken.getClaims().get("phone_number");
            if (phoneInToken != null) {
                // Normalize phone to compare
                PhoneNumber tokenPhone = PhoneNumber.of(phoneInToken);
                return tokenPhone.equals(expectedPhone);
            }
            return true;
        } catch (Exception e) {
            log.warn("Firebase ID Token verification failed: {}", e.getMessage());
            return false;
        }
    }
}
