package com.vn.bomnuocv1.data.mapper

import com.vn.bomnuocv1.data.remote.dto.PaymentResponseDto
import com.vn.bomnuocv1.domain.model.Payment

fun PaymentResponseDto.toDomain(): Payment {
    return Payment(
        id = id,
        farmerId = farmerId,
        farmerFullName = farmerFullName ?: "Nông dân",
        transactionId = transactionId,
        amount = amount,
        paymentDate = paymentDate,
        note = note,
        createdAt = createdAt
    )
}
