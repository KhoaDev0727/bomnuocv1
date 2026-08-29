package com.vn.bomnuocv1.data.remote.datasource

import com.vn.bomnuocv1.data.remote.api.AuthApiService
import com.vn.bomnuocv1.data.remote.dto.ApiResponseDto
import com.vn.bomnuocv1.data.remote.dto.AuthResponseDto
import com.vn.bomnuocv1.data.remote.dto.LoginRequestDto
import com.vn.bomnuocv1.data.remote.dto.RefreshTokenRequestDto
import com.vn.bomnuocv1.data.remote.dto.RegisterRequestDto
import com.vn.bomnuocv1.data.remote.dto.TokenResponseDto
import com.vn.bomnuocv1.data.remote.dto.UserResponseDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val apiService: AuthApiService
) {
    suspend fun register(request: RegisterRequestDto): Response<ApiResponseDto<AuthResponseDto>> {
        return apiService.register(request)
    }

    suspend fun loginWithPin(request: LoginRequestDto): Response<ApiResponseDto<AuthResponseDto>> {
        return apiService.loginWithPin(request)
    }

    suspend fun refreshToken(request: RefreshTokenRequestDto): Response<ApiResponseDto<TokenResponseDto>> {
        return apiService.refreshToken(request)
    }

    suspend fun getCurrentUser(): Response<ApiResponseDto<UserResponseDto>> {
        return apiService.getCurrentUser()
    }

    suspend fun logout(): Response<ApiResponseDto<Unit>> {
        return apiService.logout()
    }
}
