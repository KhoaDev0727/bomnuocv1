package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.User
import com.vn.bomnuocv1.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        pinCode: String,
        fullName: String,
        otpCode: String? = null,
        firebaseIdToken: String? = null,
        verificationId: String? = null
    ): Result<User> {
        val trimmedPhone = phoneNumber.trim()
        val trimmedPin = pinCode.trim()
        val trimmedName = fullName.trim()

        if (trimmedPhone.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập số điện thoại."))
        }
        if (trimmedPin.length != 4) {
            return Result.failure(IllegalArgumentException("Mã PIN phải gồm đúng 4 chữ số."))
        }
        if (trimmedName.isEmpty()) {
            return Result.failure(IllegalArgumentException("Vui lòng nhập họ và tên."))
        }

        return authRepository.register(
            phoneNumber = trimmedPhone,
            pinCode = trimmedPin,
            fullName = trimmedName,
            otpCode = otpCode,
            firebaseIdToken = firebaseIdToken,
            verificationId = verificationId
        )
    }
}
