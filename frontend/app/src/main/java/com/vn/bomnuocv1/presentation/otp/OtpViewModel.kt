package com.vn.bomnuocv1.presentation.otp

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.data.remote.datasource.FirebaseAuthDataSource
import com.vn.bomnuocv1.domain.usecase.RegisterUseCase
import com.vn.bomnuocv1.domain.usecase.SendFirebaseOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val sendFirebaseOtpUseCase: SendFirebaseOtpUseCase,
    private val registerUseCase: RegisterUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        val phoneArg: String? = savedStateHandle["phoneNumber"]
        val nameArg: String? = savedStateHandle["fullName"]
        val pinArg: String? = savedStateHandle["pinCode"]
        val vidArg: String? = savedStateHandle["verificationId"]

        val decodedName = if (!nameArg.isNullOrBlank()) {
            try {
                URLDecoder.decode(nameArg, StandardCharsets.UTF_8.toString())
            } catch (_: Exception) {
                nameArg
            }
        } else ""

        val decodedVid = if (!vidArg.isNullOrBlank()) {
            try {
                URLDecoder.decode(vidArg, StandardCharsets.UTF_8.toString())
            } catch (_: Exception) {
                vidArg
            }
        } else ""

        _uiState.update {
            it.copy(
                phoneNumber = phoneArg.orEmpty(),
                fullName = decodedName,
                pinCode = pinArg.orEmpty(),
                verificationId = decodedVid
            )
        }
        startCountdown()
    }

    fun setRegistrationData(phone: String, name: String = "", pin: String = "", verificationId: String = "") {
        _uiState.update {
            it.copy(
                phoneNumber = phone,
                fullName = name,
                pinCode = pin,
                verificationId = verificationId
            )
        }
    }

    fun onPhoneNumberChanged(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone, errorMessage = null) }
    }

    fun onOtpCodeChanged(otp: String) {
        val digitsOnly = otp.filter { it.isDigit() }
        if (digitsOnly.length <= 6) {
            _uiState.update { it.copy(otpCode = digitsOnly, errorMessage = null) }
            if (digitsOnly.length == 6) {
                onVerifyOtpClicked()
            }
        }
    }

    fun onVerifyOtpClicked() {
        val currentState = _uiState.value
        val phone = currentState.phoneNumber.trim()
        val otp = currentState.otpCode.trim()
        val name = currentState.fullName.trim()
        val pin = currentState.pinCode.trim()
        val vid = currentState.verificationId.trim()

        if (phone.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập số điện thoại.") }
            return
        }
        if (otp.length != 6) {
            _uiState.update { it.copy(errorMessage = "Mã OTP phải bao gồm đúng 6 chữ số.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }

            // If name and pin are present, complete the registration flow
            if (name.isNotEmpty() && pin.isNotEmpty()) {
                val result = registerUseCase(
                    phoneNumber = phone,
                    pinCode = pin,
                    fullName = name,
                    otpCode = otp,
                    verificationId = vid
                )
                result.fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isVerifySuccess = true,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Đăng ký tài khoản thất bại. Vui lòng thử lại!"
                            )
                        }
                    }
                )
            } else {
                val signInResult = firebaseAuthDataSource.signInWithOtp(otp, vid)
                if (signInResult.isSuccess) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isVerifySuccess = true,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = signInResult.exceptionOrNull()?.message
                                ?: "Mã OTP không chính xác hoặc đã hết hạn."
                        )
                    }
                }
            }
        }
    }

    fun onResendOtpClicked(activity: Activity? = null) {
        val phone = _uiState.value.phoneNumber.trim()
        if (phone.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập số điện thoại để nhận mã.") }
            return
        }
        if (activity == null) {
            _uiState.update { it.copy(errorMessage = "Không thể khởi tạo môi trường gửi lại mã.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }

        sendFirebaseOtpUseCase(
            activity = activity,
            phoneNumber = phone,
            isResend = true,
            onCodeSent = { newVid ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        verificationId = newVid,
                        infoMessage = "Mã xác thực mới đã được gửi thành công.",
                        otpCode = ""
                    )
                }
                startCountdown()
            },
            onAutoVerified = { token ->
                // If auto-verified on resend
                _uiState.update { it.copy(isLoading = false) }
                onVerifyOtpClicked()
            },
            onError = { errorMsg ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMsg
                    )
                }
            }
        )
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            _uiState.update { it.copy(resendCountdown = 60, canResend = false) }
            for (i in 59 downTo 0) {
                delay(1000L)
                _uiState.update { it.copy(resendCountdown = i) }
            }
            _uiState.update { it.copy(canResend = true) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}
