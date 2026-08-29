package com.vn.bomnuocv1.domain.model

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresInMs: Long = 0L
)

data class SessionInfo(
    val isLoggedIn: Boolean,
    val user: User? = null,
    val isOfflineMode: Boolean = false
)
