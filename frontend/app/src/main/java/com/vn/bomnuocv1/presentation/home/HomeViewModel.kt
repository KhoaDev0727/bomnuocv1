package com.vn.bomnuocv1.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.model.DashboardSummary
import com.vn.bomnuocv1.domain.model.RecentTransaction
import com.vn.bomnuocv1.domain.usecase.GetDashboardSummaryUseCase
import com.vn.bomnuocv1.domain.usecase.GetSavedSessionUseCase
import com.vn.bomnuocv1.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSavedSessionUseCase: GetSavedSessionUseCase,
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val session = getSavedSessionUseCase()
            _uiState.update {
                it.copy(
                    user = session.user,
                    isOfflineMode = session.isOfflineMode
                )
            }

            // Fetch dashboard summary
            val dashboardResult = getDashboardSummaryUseCase()
            dashboardResult.onSuccess { summary ->
                _uiState.update {
                    it.copy(
                        dashboardSummary = summary,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                // Provide initial starter preview if network is offline or unseeded
                val fallbackSummary = createFallbackSummary()
                _uiState.update {
                    it.copy(
                        dashboardSummary = it.dashboardSummary ?: fallbackSummary,
                        isLoading = false,
                        errorMessage = if (it.isOfflineMode) null else error.message
                    )
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val dashboardResult = getDashboardSummaryUseCase()
            dashboardResult.onSuccess { summary ->
                _uiState.update {
                    it.copy(
                        dashboardSummary = summary,
                        isRefreshing = false
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun onTabSelected(tab: HomeBottomTab) {
        _uiState.update { it.copy(selectedTab = tab) }
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

    private fun createFallbackSummary(): DashboardSummary {
        return DashboardSummary(
            totalUncollectedDebt = BigDecimal("15450000"),
            formattedTotalDebt = "15.450.000đ",
            todayPumpCount = 12L,
            recentTransactions = listOf(
                RecentTransaction(
                    id = "demo-1",
                    type = "PUMP",
                    farmerName = "Nguyễn Văn An",
                    details = "Bơm 2.5 công • 08:30 sáng",
                    amount = BigDecimal("500000"),
                    formattedAmount = "+ 500.000đ",
                    statusBadge = "Ghi nợ",
                    createdAt = null
                ),
                RecentTransaction(
                    id = "demo-2",
                    type = "PAYMENT",
                    farmerName = "Lê Thị Bé",
                    details = "Thu nợ cũ • Hôm qua",
                    amount = BigDecimal("-1200000"),
                    formattedAmount = "- 1.200.000đ",
                    statusBadge = "Đã thu",
                    createdAt = null
                )
            )
        )
    }
}
