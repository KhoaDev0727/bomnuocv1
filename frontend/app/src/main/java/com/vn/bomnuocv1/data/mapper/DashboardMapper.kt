package com.vn.bomnuocv1.data.mapper

import com.vn.bomnuocv1.data.remote.dto.DashboardSummaryDto
import com.vn.bomnuocv1.data.remote.dto.RecentTransactionDto
import com.vn.bomnuocv1.domain.model.DashboardSummary
import com.vn.bomnuocv1.domain.model.RecentTransaction

fun DashboardSummaryDto.toDomain(): DashboardSummary {
    return DashboardSummary(
        totalUncollectedDebt = totalUncollectedDebt,
        formattedTotalDebt = formattedTotalDebt,
        todayPumpCount = todayPumpCount,
        recentTransactions = recentTransactions.map { it.toDomain() }
    )
}

fun RecentTransactionDto.toDomain(): RecentTransaction {
    return RecentTransaction(
        id = id,
        type = type,
        farmerName = farmerName,
        details = details,
        amount = amount,
        formattedAmount = formattedAmount,
        statusBadge = statusBadge,
        createdAt = createdAt
    )
}
