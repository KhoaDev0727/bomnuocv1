package com.vn.bomnuocv1.presentation.login

data class LoginUiState(
    val phoneNumber: String = "",
    val pinCode: String = "",
    val maxPinDigits: Int = 4,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false
)
