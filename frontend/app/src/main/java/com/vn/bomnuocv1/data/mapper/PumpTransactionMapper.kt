package com.vn.bomnuocv1.data.mapper

import com.vn.bomnuocv1.data.remote.dto.PumpTransactionResponseDto
import com.vn.bomnuocv1.domain.model.PaymentStatus
import com.vn.bomnuocv1.domain.model.PumpTransaction
import java.math.BigDecimal

fun PumpTransactionResponseDto.toDomain(): PumpTransaction {
    val status = when (paymentStatus?.uppercase()) {
        "PAID" -> PaymentStatus.PAID
        "PARTIAL" -> PaymentStatus.PARTIAL
        else -> PaymentStatus.UNPAID
    }

    return PumpTransaction(
        id = id,
        farmerId = farmerId,
        farmerFullName = farmerFullName ?: "Nông dân",
        farmerPhone = farmerPhone,
        pricingRuleId = pricingRuleId,
        transactionDate = transactionDate,
        quantity = quantity,
        quantityUnit = quantityUnit,
        unitPrice = unitPrice,
        amountDue = amountDue,
        paidAmount = paidAmount ?: BigDecimal.ZERO,
        remainingDebt = remainingDebt ?: amountDue,
        paymentStatus = status,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
