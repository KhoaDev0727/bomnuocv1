package com.vn.bomnuocv1.domain.repository

import com.vn.bomnuocv1.domain.model.Payment
import java.math.BigDecimal

data class CreatePaymentParams(
    val farmerId: String,
    val transactionId: String?,
    val amount: BigDecimal,
    val paymentDate: String,
    val note: String?
)

interface PaymentRepository {

    suspend fun recordPayment(
        params: CreatePaymentParams
    ): Result<Payment>

    suspend fun getPayments(
        farmerId: String? = null,
        transactionId: String? = null
    ): Result<List<Payment>>
}
