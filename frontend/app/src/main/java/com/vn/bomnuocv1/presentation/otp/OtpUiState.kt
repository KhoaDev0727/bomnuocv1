package com.vn.bomnuocv1.presentation.otp

data class OtpUiState(
    val phoneNumber: String = "",
    val fullName: String = "",
    val pinCode: String = "",
    val verificationId: String = "",
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val isVerifySuccess: Boolean = false,
    val resendCountdown: Int = 60,
    val canResend: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

