package com.vn.bomnuocv1.data.remote.datasource

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource @Inject constructor() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun getStoredVerificationId(): String? = storedVerificationId

    fun setVerificationInfo(verificationId: String, token: PhoneAuthProvider.ForceResendingToken?) {
        this.storedVerificationId = verificationId
        this.resendToken = token
    }

    fun verifyPhoneNumber(
        activity: Activity,
        phoneNumber: String,
        isResend: Boolean = false,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onVerificationFailed: (FirebaseException) -> Unit
    ) {
        val formattedPhone = formatToE164(phoneNumber)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                onVerificationCompleted(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onVerificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
                onCodeSent(verificationId, token)
            }
        }

        val builder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(formattedPhone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)

        if (isResend && resendToken != null) {
            builder.setForceResendingToken(resendToken!!)
        }

        PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

    suspend fun signInWithOtp(otpCode: String, verificationId: String? = storedVerificationId): Result<String> {
        return try {
            val vId = verificationId ?: storedVerificationId
                ?: return Result.failure(IllegalStateException("Không tìm thấy mã xác thực phiên (Verification ID)."))

            val credential = PhoneAuthProvider.getCredential(vId, otpCode)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(IllegalStateException("Đăng nhập Firebase thất bại."))

            val idTokenResult = firebaseUser.getIdToken(false).await()
            val token = idTokenResult.token
                ?: return Result.failure(IllegalStateException("Không lấy được Firebase ID Token."))

            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential): Result<String> {
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(IllegalStateException("Đăng nhập Firebase thất bại."))
            val idTokenResult = firebaseUser.getIdToken(false).await()
            val token = idTokenResult.token
                ?: return Result.failure(IllegalStateException("Không lấy được Firebase ID Token."))
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun formatToE164(phone: String): String {
        val cleaned = phone.replace(Regex("[^0-9+]"), "")
        return when {
            cleaned.startsWith("+") -> cleaned
            cleaned.startsWith("0") -> "+84" + cleaned.substring(1)
            cleaned.startsWith("84") -> "+$cleaned"
            else -> "+84$cleaned"
        }
    }
}
