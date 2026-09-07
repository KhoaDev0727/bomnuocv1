package com.vn.bomnuocv1.presentation.main

import androidx.lifecycle.ViewModel
import com.vn.bomnuocv1.core.network.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    val sessionExpiredEvent: SharedFlow<String> = sessionManager.sessionExpiredEvent

    fun resetExpirationState() {
        sessionManager.resetExpirationState()
    }
}
