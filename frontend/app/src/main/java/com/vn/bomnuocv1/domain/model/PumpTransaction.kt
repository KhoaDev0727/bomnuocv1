package com.vn.bomnuocv1.domain.model

import java.math.BigDecimal

data class PumpTransaction(
    val id: String,
    val farmerId: String,
    val farmerFullName: String,
    val farmerPhone: String?,
    val pricingRuleId: String?,
    val transactionDate: String,
    val quantity: BigDecimal,
    val quantityUnit: String,
    val unitPrice: BigDecimal,
    val amountDue: BigDecimal,
    val paidAmount: BigDecimal,
    val remainingDebt: BigDecimal,
    val paymentStatus: PaymentStatus,
    val note: String?,
    val createdAt: String?,
    val updatedAt: String?
)
