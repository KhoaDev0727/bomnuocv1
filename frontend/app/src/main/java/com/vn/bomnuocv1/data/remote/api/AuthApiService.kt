package com.vn.bomnuocv1.data.remote.api

import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.AuthResponseDto
import com.vn.bomnuocv1.data.remote.dto.LoginRequestDto
import com.vn.bomnuocv1.data.remote.dto.RefreshTokenRequestDto
import com.vn.bomnuocv1.data.remote.dto.RegisterRequestDto
import com.vn.bomnuocv1.data.remote.dto.TokenResponseDto
import com.vn.bomnuocv1.data.remote.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): Response<ApiResponseDto<AuthResponseDto>>

    @POST("api/v1/auth/login-pin")
    suspend fun loginWithPin(
        @Body request: LoginRequestDto
    ): Response<ApiResponseDto<AuthResponseDto>>

    @POST("api/v1/auth/refresh-token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): Response<ApiResponseDto<TokenResponseDto>>

    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): Response<ApiResponseDto<UserResponseDto>>

    @POST("api/v1/auth/logout")
    suspend fun logout(): Response<ApiResponseDto<Unit>>
}
