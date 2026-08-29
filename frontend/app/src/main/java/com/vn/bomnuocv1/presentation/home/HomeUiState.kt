package com.vn.bomnuocv1.presentation.home

import com.vn.bomnuocv1.domain.model.User

data class HomeUiState(
    val user: User? = null,
    val isOfflineMode: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)
