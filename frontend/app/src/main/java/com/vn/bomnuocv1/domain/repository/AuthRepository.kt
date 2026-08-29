package com.vn.bomnuocv1.domain.repository

import com.vn.bomnuocv1.domain.model.SessionInfo
import com.vn.bomnuocv1.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(
        phoneNumber: String,
        pinCode: String,
        fullName: String,
        otpCode: String? = null,
        firebaseIdToken: String? = null,
        verificationId: String? = null
    ): Result<User>

    fun requestFirebaseOtp(
        activity: android.app.Activity,
        phoneNumber: String,
        isResend: Boolean = false,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: (firebaseIdToken: String) -> Unit,
        onError: (String) -> Unit
    )

    suspend fun loginWithPin(
        phoneNumber: String,
        pinCode: String
    ): Result<User>

    suspend fun getSavedSession(): SessionInfo

    fun observeSession(): Flow<SessionInfo>

    suspend fun getRememberedPhoneNumber(): String?

    suspend fun saveRememberedPhoneNumber(phoneNumber: String)

    suspend fun logout(): Result<Unit>
}
