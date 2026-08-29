package com.vn.bomnuocv1.data.mapper

import com.vn.bomnuocv1.data.remote.dto.AuthResponseDto
import com.vn.bomnuocv1.data.remote.dto.TokenResponseDto
import com.vn.bomnuocv1.data.remote.dto.UserResponseDto
import com.vn.bomnuocv1.domain.model.AuthTokens
import com.vn.bomnuocv1.domain.model.User

object AuthDataMapper {

    fun toDomainUser(dto: UserResponseDto): User {
        return User(
            id = dto.id,
            phoneNumber = dto.phoneNumber,
            fullName = dto.fullName,
            roleCode = dto.roleCode,
            roleName = dto.roleName,
            active = dto.active
        )
    }

    fun toDomainTokens(dto: TokenResponseDto): AuthTokens {
        return AuthTokens(
            accessToken = dto.accessToken,
            refreshToken = dto.refreshToken,
            tokenType = dto.tokenType,
            expiresInMs = dto.expiresInMs
        )
    }
}
