package com.vn.bomnuocv1.presentation.register

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.usecase.RegisterUseCase
import com.vn.bomnuocv1.domain.usecase.SendFirebaseOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sendFirebaseOtpUseCase: SendFirebaseOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onPhoneNumberChanged(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone, errorMessage = null) }
    }

    fun onFullNameChanged(name: String) {
        _uiState.update { it.copy(fullName = name, errorMessage = null) }
    }

    fun onPinCodeChanged(pin: String) {
        if (pin.length <= 4) {
            _uiState.update { it.copy(pinCode = pin, errorMessage = null) }
        }
    }

    fun onConfirmPinCodeChanged(confirmPin: String) {
        if (confirmPin.length <= 4) {
            _uiState.update { it.copy(confirmPinCode = confirmPin, errorMessage = null) }
        }
    }

    fun onRegisterClicked(activity: Activity? = null) {
        val currentState = _uiState.value
        val phone = currentState.phoneNumber.trim()
        val name = currentState.fullName.trim()
        val pin = currentState.pinCode.trim()
        val confirmPin = currentState.confirmPinCode.trim()

        if (phone.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập số điện thoại.") }
            return
        }
        if (name.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập họ và tên chủ trạm.") }
            return
        }
        if (pin.length != 4) {
            _uiState.update { it.copy(errorMessage = "Mã PIN phải gồm đúng 4 chữ số.") }
            return
        }
        if (pin != confirmPin) {
            _uiState.update { it.copy(errorMessage = "Mã PIN xác nhận không trùng khớp.") }
            return
        }
        if (activity == null) {
            _uiState.update { it.copy(errorMessage = "Không thể khởi tạo môi trường xác thực Firebase.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        sendFirebaseOtpUseCase(
            activity = activity,
            phoneNumber = phone,
            isResend = false,
            onCodeSent = { verificationId ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOtpSent = true,
                        verificationId = verificationId,
                        errorMessage = null
                    )
                }
            },
            onAutoVerified = { firebaseIdToken ->
                viewModelScope.launch {
                    val result = registerUseCase(
                        phoneNumber = phone,
                        pinCode = pin,
                        fullName = name,
                        firebaseIdToken = firebaseIdToken
                    )
                    result.fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isRegisterSuccess = true,
                                    errorMessage = null
                                )
                            }
                        },
                        onFailure = { err ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = err.message ?: "Đăng ký tài khoản thất bại."
                                )
                            }
                        }
                    )
                }
            },
            onError = { errorMsg ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOtpSent = false,
                        errorMessage = errorMsg
                    )
                }
            }
        )
    }

    fun resetOtpSent() {
        _uiState.update { it.copy(isOtpSent = false) }
    }
}
