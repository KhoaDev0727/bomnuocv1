package com.vn.bomnuocv1.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class DashboardSummaryDto(
    @SerializedName("totalUncollectedDebt") val totalUncollectedDebt: BigDecimal,
    @SerializedName("formattedTotalDebt") val formattedTotalDebt: String,
    @SerializedName("todayPumpCount") val todayPumpCount: Long,
    @SerializedName("recentTransactions") val recentTransactions: List<RecentTransactionDto>
)

data class RecentTransactionDto(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("farmerName") val farmerName: String,
    @SerializedName("details") val details: String,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("formattedAmount") val formattedAmount: String,
    @SerializedName("statusBadge") val statusBadge: String,
    @SerializedName("createdAt") val createdAt: String?
)
