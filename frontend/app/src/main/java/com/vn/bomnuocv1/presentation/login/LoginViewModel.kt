package com.vn.bomnuocv1.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.usecase.GetSavedSessionUseCase
import com.vn.bomnuocv1.domain.usecase.LoginWithPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithPinUseCase: LoginWithPinUseCase,
    private val getSavedSessionUseCase: GetSavedSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        loadRememberedPhone()
    }

    private fun loadRememberedPhone() {
        viewModelScope.launch {
            val phone = getSavedSessionUseCase.getRememberedPhone()
            if (!phone.isNullOrBlank()) {
                _uiState.update { it.copy(phoneNumber = phone) }
            }
        }
    }

    fun onPhoneNumberChanged(phone: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phone,
                errorMessage = null
            )
        }
    }

    fun onPinCodeChanged(pin: String) {
        val filtered = pin.filter { it.isDigit() }
        if (filtered.length <= 6) {
            _uiState.update {
                it.copy(
                    pinCode = filtered,
                    errorMessage = null
                )
            }
        }
    }

    fun onPinDigit(digit: String) {
        if (_uiState.value.pinCode.length >= 4) return
        val newPin = _uiState.value.pinCode + digit
        _uiState.update {
            it.copy(
                pinCode = newPin,
                errorMessage = null
            )
        }
        // Auto-login immediately when exact 4 digits are entered
        if (newPin.length == 4 && _uiState.value.phoneNumber.isNotBlank()) {
            login(auto = true)
        }
    }

    fun onPinBackspace() {
        val current = _uiState.value.pinCode
        if (current.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    pinCode = current.dropLast(1),
                    errorMessage = null
                )
            }
        }
    }

    fun onPinClear() {
        _uiState.update {
            it.copy(
                pinCode = "",
                errorMessage = null
            )
        }
    }

    fun onLoginClicked() {
        login(auto = false)
    }

    private fun login(auto: Boolean) {
        val currentState = _uiState.value
        val phone = currentState.phoneNumber.trim()
        val pin = currentState.pinCode.trim()

        if (phone.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập số điện thoại.") }
            return
        }
        if (pin.length < 4) {
            if (!auto) {
                _uiState.update { it.copy(errorMessage = "Mã PIN phải gồm từ 4 đến 6 chữ số.") }
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginWithPinUseCase(phone, pin)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Đăng nhập thất bại. Vui lòng thử lại!"
                        )
                    }
                }
            )
        }
    }
}
