package com.vn.bomnuocv1.domain.usecase

import android.app.Activity
import com.vn.bomnuocv1.domain.repository.AuthRepository
import javax.inject.Inject

class SendFirebaseOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        activity: Activity,
        phoneNumber: String,
        isResend: Boolean = false,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: (firebaseIdToken: String) -> Unit,
        onError: (String) -> Unit
    ) {
        val trimmed = phoneNumber.trim()
        if (trimmed.isEmpty()) {
            onError("Vui lòng nhập số điện thoại.")
            return
        }
        authRepository.requestFirebaseOtp(
            activity = activity,
            phoneNumber = trimmed,
            isResend = isResend,
            onCodeSent = onCodeSent,
            onAutoVerified = onAutoVerified,
            onError = onError
        )
    }
}
