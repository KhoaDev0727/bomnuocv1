package com.vn.bomnuocv1.domain.model

import java.math.BigDecimal

data class RecentTransaction(
    val id: String,
    val type: String, // "PUMP" or "PAYMENT"
    val farmerName: String,
    val details: String,
    val amount: BigDecimal,
    val formattedAmount: String,
    val statusBadge: String,
    val createdAt: String?
)
