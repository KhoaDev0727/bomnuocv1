package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.User
import com.vn.bomnuocv1.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithPinUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        pinCode: String
    ): Result<User> {
        val trimmedPhone = phoneNumber.trim()
        val trimmedPin = pinCode.trim()

        if (trimmedPhone.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập số điện thoại."))
        }
        if (trimmedPin.length != 4) {
            return Result.failure(IllegalArgumentException("Mã PIN phải gồm đúng 4 chữ số."))
        }

        return authRepository.loginWithPin(trimmedPhone, trimmedPin)
    }
}
