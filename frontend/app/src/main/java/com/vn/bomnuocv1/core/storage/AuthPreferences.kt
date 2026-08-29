package com.vn.bomnuocv1.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vn.bomnuocv1.domain.model.AuthTokens
import com.vn.bomnuocv1.domain.model.SessionInfo
import com.vn.bomnuocv1.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bomnuoc_auth_prefs")

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_REMEMBERED_PHONE = stringPreferencesKey("remembered_phone")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        // Offline User Cache
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        private val KEY_USER_FULL_NAME = stringPreferencesKey("user_full_name")
        private val KEY_USER_ROLE_CODE = stringPreferencesKey("user_role_code")
        private val KEY_USER_ROLE_NAME = stringPreferencesKey("user_role_name")
        private val KEY_USER_ACTIVE = booleanPreferencesKey("user_active")
    }

    val sessionFlow: Flow<SessionInfo> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isLoggedIn = preferences[KEY_IS_LOGGED_IN] ?: false
            if (isLoggedIn) {
                val userId = preferences[KEY_USER_ID] ?: ""
                val phone = preferences[KEY_USER_PHONE] ?: ""
                val name = preferences[KEY_USER_FULL_NAME] ?: ""
                val roleCode = preferences[KEY_USER_ROLE_CODE] ?: "owner"
                val roleName = preferences[KEY_USER_ROLE_NAME] ?: "Chủ trạm"
                val active = preferences[KEY_USER_ACTIVE] ?: true

                val user = User(
                    id = userId,
                    phoneNumber = phone,
                    fullName = name,
                    roleCode = roleCode,
                    roleName = roleName,
                    active = active
                )
                SessionInfo(isLoggedIn = true, user = user, isOfflineMode = false)
            } else {
                SessionInfo(isLoggedIn = false, user = null, isOfflineMode = false)
            }
        }

    suspend fun saveSession(user: User, tokens: AuthTokens) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_LOGGED_IN] = true
            preferences[KEY_ACCESS_TOKEN] = tokens.accessToken
            preferences[KEY_REFRESH_TOKEN] = tokens.refreshToken
            preferences[KEY_REMEMBERED_PHONE] = user.phoneNumber

            // Offline user cache
            preferences[KEY_USER_ID] = user.id
            preferences[KEY_USER_PHONE] = user.phoneNumber
            preferences[KEY_USER_FULL_NAME] = user.fullName
            preferences[KEY_USER_ROLE_CODE] = user.roleCode
            preferences[KEY_USER_ROLE_NAME] = user.roleName
            preferences[KEY_USER_ACTIVE] = user.active
        }
    }

    suspend fun getAccessToken(): String? {
        val prefs = context.dataStore.data.firstOrNull() ?: return null
        return prefs[KEY_ACCESS_TOKEN]
    }

    suspend fun getRefreshToken(): String? {
        val prefs = context.dataStore.data.firstOrNull() ?: return null
        return prefs[KEY_REFRESH_TOKEN]
    }

    suspend fun getRememberedPhone(): String? {
        val prefs = context.dataStore.data.firstOrNull() ?: return null
        return prefs[KEY_REMEMBERED_PHONE]
    }

    suspend fun saveRememberedPhone(phone: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_REMEMBERED_PHONE] = phone
        }
    }

    suspend fun getSavedSession(): SessionInfo {
        val prefs = context.dataStore.data.firstOrNull() ?: return SessionInfo(false)
        val isLoggedIn = prefs[KEY_IS_LOGGED_IN] ?: false
        if (!isLoggedIn) return SessionInfo(false)

        val userId = prefs[KEY_USER_ID] ?: ""
        val phone = prefs[KEY_USER_PHONE] ?: ""
        val name = prefs[KEY_USER_FULL_NAME] ?: ""
        val roleCode = prefs[KEY_USER_ROLE_CODE] ?: "owner"
        val roleName = prefs[KEY_USER_ROLE_NAME] ?: "Chủ trạm"
        val active = prefs[KEY_USER_ACTIVE] ?: true

        val user = User(
            id = userId,
            phoneNumber = phone,
            fullName = name,
            roleCode = roleCode,
            roleName = roleName,
            active = active
        )
        return SessionInfo(isLoggedIn = true, user = user, isOfflineMode = true)
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_LOGGED_IN] = false
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_REFRESH_TOKEN)
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_USER_PHONE)
            preferences.remove(KEY_USER_FULL_NAME)
            preferences.remove(KEY_USER_ROLE_CODE)
            preferences.remove(KEY_USER_ROLE_NAME)
            preferences.remove(KEY_USER_ACTIVE)
            // Note: We keep KEY_REMEMBERED_PHONE so user doesn't have to re-type their phone number
        }
    }
}
