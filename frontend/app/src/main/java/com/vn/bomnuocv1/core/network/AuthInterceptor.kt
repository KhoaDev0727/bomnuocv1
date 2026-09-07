package com.vn.bomnuocv1.core.network

import com.vn.bomnuocv1.core.storage.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth header for authentication public endpoints
        val path = originalRequest.url.encodedPath
        val isPublicAuthEndpoint = path.contains("/auth/login-pin") ||
            path.contains("/auth/register") ||
            path.contains("/auth/send-otp") ||
            path.contains("/auth/verify-otp") ||
            path.contains("/auth/refresh-token")

        if (isPublicAuthEndpoint) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { authPreferences.getAccessToken() }
        val authenticatedRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(authenticatedRequest)

        // If backend returns 401 Unauthorized, the session has expired or token is invalid
        if (response.code == 401) {
            runBlocking {
                authPreferences.clearSession()
            }
            sessionManager.triggerSessionExpired("Phiên đăng nhập của bạn đã hết hạn. Vui lòng đăng nhập lại.")
        }

        return response
    }
}
