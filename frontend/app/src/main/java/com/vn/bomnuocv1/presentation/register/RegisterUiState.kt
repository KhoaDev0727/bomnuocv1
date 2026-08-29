package com.vn.bomnuocv1.presentation.register

data class RegisterUiState(
    val phoneNumber: String = "",
    val fullName: String = "",
    val pinCode: String = "",
    val confirmPinCode: String = "",
    val otpCode: String = "",
    val verificationId: String = "",
    val isOtpSent: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccess: Boolean = false
)
