package com.vn.bomnuocv1.data.local.datasource

import com.vn.bomnuocv1.core.storage.AuthPreferences
import com.vn.bomnuocv1.domain.model.AuthTokens
import com.vn.bomnuocv1.domain.model.SessionInfo
import com.vn.bomnuocv1.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthLocalDataSource @Inject constructor(
    private val authPreferences: AuthPreferences
) {
    val sessionFlow: Flow<SessionInfo> = authPreferences.sessionFlow

    suspend fun saveSession(user: User, tokens: AuthTokens) {
        authPreferences.saveSession(user, tokens)
    }

    suspend fun getSavedSession(): SessionInfo {
        return authPreferences.getSavedSession()
    }

    suspend fun getRememberedPhone(): String? {
        return authPreferences.getRememberedPhone()
    }

    suspend fun saveRememberedPhone(phone: String) {
        authPreferences.saveRememberedPhone(phone)
    }

    suspend fun clearSession() {
        authPreferences.clearSession()
    }
}
