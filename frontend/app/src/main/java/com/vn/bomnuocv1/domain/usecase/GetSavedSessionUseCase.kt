package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.SessionInfo
import com.vn.bomnuocv1.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): SessionInfo {
        return authRepository.getSavedSession()
    }

    fun observe(): Flow<SessionInfo> {
        return authRepository.observeSession()
    }

    suspend fun getRememberedPhone(): String? {
        return authRepository.getRememberedPhoneNumber()
    }
}
