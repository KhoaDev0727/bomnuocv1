package com.vn.bomnuocv1.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponseDto<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?,
    @SerializedName("timestamp") val timestamp: String?
)

data class RegisterRequestDto(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("pinCode") val pinCode: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("roleCode") val roleCode: String? = "owner",
    @SerializedName("otpCode") val otpCode: String? = null,
    @SerializedName("firebaseIdToken") val firebaseIdToken: String? = null
)

data class LoginRequestDto(
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("pinCode") val pinCode: String
)

data class RefreshTokenRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)

data class AuthResponseDto(
    @SerializedName("user") val user: UserResponseDto,
    @SerializedName("tokens") val tokens: TokenResponseDto
)

data class UserResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("roleCode") val roleCode: String,
    @SerializedName("roleName") val roleName: String,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: String?
)

data class TokenResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("expiresInMs") val expiresInMs: Long
)
