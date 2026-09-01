package com.vn.bomnuocv1.presentation.home

import com.vn.bomnuocv1.domain.model.DashboardSummary
import com.vn.bomnuocv1.domain.model.User

enum class HomeBottomTab {
    HOME,
    PUMP_LOG,
    DEBT_LEDGER,
    SETTINGS
}

data class HomeUiState(
    val user: User? = null,
    val isOfflineMode: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoggedOut: Boolean = false,
    val dashboardSummary: DashboardSummary? = null,
    val selectedTab: HomeBottomTab = HomeBottomTab.HOME,
    val errorMessage: String? = null
)
