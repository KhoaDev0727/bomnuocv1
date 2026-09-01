package com.vn.bomnuocv1.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.model.User
import com.vn.bomnuocv1.domain.usecase.GetSavedSessionUseCase
import com.vn.bomnuocv1.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val user: User? = null,
    val isOfflineMode: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSavedSessionUseCase: GetSavedSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            val session = getSavedSessionUseCase()
            _uiState.update {
                it.copy(
                    user = session.user,
                    isOfflineMode = session.isOfflineMode,
                    isLoading = false
                )
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            logoutUseCase()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoggedOut = true
                )
            }
        }
    }
}
