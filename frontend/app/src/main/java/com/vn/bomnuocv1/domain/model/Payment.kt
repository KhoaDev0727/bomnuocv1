package com.vn.bomnuocv1.domain.model

import java.math.BigDecimal

data class Payment(
    val id: String,
    val farmerId: String,
    val farmerFullName: String,
    val transactionId: String?,
    val amount: BigDecimal,
    val paymentDate: String,
    val note: String?,
    val createdAt: String?
)
