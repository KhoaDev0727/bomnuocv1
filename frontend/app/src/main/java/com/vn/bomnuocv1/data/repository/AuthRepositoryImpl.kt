package com.vn.bomnuocv1.data.repository

import com.google.gson.Gson
import com.vn.bomnuocv1.data.local.datasource.AuthLocalDataSource
import com.vn.bomnuocv1.data.mapper.AuthDataMapper
import com.vn.bomnuocv1.data.remote.datasource.AuthRemoteDataSource
import com.vn.bomnuocv1.data.remote.datasource.FirebaseAuthDataSource
import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.LoginRequestDto
import com.vn.bomnuocv1.data.remote.dto.RegisterRequestDto
import com.vn.bomnuocv1.domain.model.SessionInfo
import com.vn.bomnuocv1.domain.model.User
import com.vn.bomnuocv1.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val gson: Gson
) : AuthRepository {

    override suspend fun register(
        phoneNumber: String,
        pinCode: String,
        fullName: String,
        otpCode: String?,
        firebaseIdToken: String?,
        verificationId: String?
    ): Result<User> {
        return try {
            val resolvedToken = if (!firebaseIdToken.isNullOrBlank()) {
                firebaseIdToken
            } else if (!otpCode.isNullOrBlank()) {
                val signInResult = firebaseAuthDataSource.signInWithOtp(otpCode, verificationId)
                if (signInResult.isFailure) {
                    val err = signInResult.exceptionOrNull()?.message
                        ?: "Mã OTP không chính xác hoặc đã hết hạn."
                    return Result.failure(Exception(err))
                }
                signInResult.getOrNull()
            } else null

            val request = RegisterRequestDto(
                phoneNumber = phoneNumber,
                pinCode = pinCode,
                fullName = fullName,
                roleCode = "owner",
                otpCode = otpCode,
                firebaseIdToken = resolvedToken
            )
            val response = remoteDataSource.register(request)

            if (response.isSuccessful && response.body()?.data != null) {
                val authData = response.body()!!.data!!
                val user = AuthDataMapper.toDomainUser(authData.user)
                val tokens = AuthDataMapper.toDomainTokens(authData.tokens)

                // Save session offline
                localDataSource.saveSession(user, tokens)
                localDataSource.saveRememberedPhone(user.phoneNumber)

                Result.success(user)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: response.body()?.message
                    ?: "Đăng ký thất bại. Vui lòng kiểm tra lại thông tin!"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng!"))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Đã có lỗi xảy ra trong quá trình đăng ký."))
        }
    }

    override fun requestFirebaseOtp(
        activity: android.app.Activity,
        phoneNumber: String,
        isResend: Boolean,
        onCodeSent: (verificationId: String) -> Unit,
        onAutoVerified: (firebaseIdToken: String) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuthDataSource.verifyPhoneNumber(
            activity = activity,
            phoneNumber = phoneNumber,
            isResend = isResend,
            onCodeSent = { verificationId, _ ->
                onCodeSent(verificationId)
            },
            onVerificationCompleted = { credential ->
                // Instant auto verification
                try {
                    val tokenResult = kotlinx.coroutines.runBlocking {
                        firebaseAuthDataSource.signInWithCredential(credential)
                    }
                    if (tokenResult.isSuccess) {
                        onAutoVerified(tokenResult.getOrNull().orEmpty())
                    } else {
                        onError(tokenResult.exceptionOrNull()?.message ?: "Xác thực tự động thất bại.")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Xác thực tự động thất bại.")
                }
            },
            onVerificationFailed = { e ->
                val friendlyMessage = when {
                    e.message?.contains("quota", ignoreCase = true) == true ->
                        "Đã vượt quá giới hạn gửi SMS của Firebase. Vui lòng thử lại sau hoặc dùng số test!"
                    e.message?.contains("invalid", ignoreCase = true) == true ->
                        "Số điện thoại không hợp lệ theo định dạng Firebase."
                    e.message?.contains("app-not-authorized", ignoreCase = true) == true ->
                        "Chưa cấu hình SHA-1 / SHA-256 trên Firebase Console."
                    else -> e.message ?: "Gửi mã OTP qua Firebase thất bại."
                }
                onError(friendlyMessage)
            }
        )
    }

    override suspend fun loginWithPin(
        phoneNumber: String,
        pinCode: String
    ): Result<User> {
        return try {
            val request = LoginRequestDto(
                phoneNumber = phoneNumber,
                pinCode = pinCode
            )
            val response = remoteDataSource.loginWithPin(request)

            if (response.isSuccessful && response.body()?.data != null) {
                val authData = response.body()!!.data!!
                val user = AuthDataMapper.toDomainUser(authData.user)
                val tokens = AuthDataMapper.toDomainTokens(authData.tokens)

                // Save session offline & remember phone
                localDataSource.saveSession(user, tokens)
                localDataSource.saveRememberedPhone(user.phoneNumber)

                Result.success(user)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: response.body()?.message
                    ?: "Số điện thoại hoặc mã PIN không chính xác!"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: IOException) {
            // Offline fallback check: If offline and previous session matches phone
            val savedSession = localDataSource.getSavedSession()
            if (savedSession.isLoggedIn && savedSession.user?.phoneNumber == phoneNumber) {
                Result.success(savedSession.user)
            } else {
                Result.failure(Exception("Không có kết nối mạng và chưa có phiên offline tương ứng."))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Đã có lỗi xảy ra khi đăng nhập."))
        }
    }

    override suspend fun getSavedSession(): SessionInfo {
        return localDataSource.getSavedSession()
    }

    override fun observeSession(): Flow<SessionInfo> {
        return localDataSource.sessionFlow
    }

    override suspend fun getRememberedPhoneNumber(): String? {
        return localDataSource.getRememberedPhone()
    }

    override suspend fun saveRememberedPhoneNumber(phoneNumber: String) {
        localDataSource.saveRememberedPhone(phoneNumber)
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            try {
                remoteDataSource.logout()
            } catch (_: Exception) {
                // Ignore network errors on logout to allow local offline logout
            }
            localDataSource.clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseErrorMessage(errorJson: String?): String? {
        if (errorJson.isNullOrBlank()) return null
        return try {
            val parsed = gson.fromJson(errorJson, ApiResponseDto::class.java)
            parsed.message
        } catch (_: Exception) {
            null
        }
    }
}
