package com.vn.bomnuocv1.domain.model

import java.math.BigDecimal

data class DashboardSummary(
    val totalUncollectedDebt: BigDecimal,
    val formattedTotalDebt: String,
    val todayPumpCount: Long,
    val recentTransactions: List<RecentTransaction>
)
