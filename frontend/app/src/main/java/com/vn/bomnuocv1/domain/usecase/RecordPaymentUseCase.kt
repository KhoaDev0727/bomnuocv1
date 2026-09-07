package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.Payment
import com.vn.bomnuocv1.domain.repository.CreatePaymentParams
import com.vn.bomnuocv1.domain.repository.PaymentRepository
import java.math.BigDecimal
import javax.inject.Inject

class RecordPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(
        farmerId: String,
        transactionId: String?,
        amount: BigDecimal,
        paymentDate: String,
        note: String?
    ): Result<Payment> {
        val params = CreatePaymentParams(
            farmerId = farmerId,
            transactionId = transactionId,
            amount = amount,
            paymentDate = paymentDate,
            note = note
        )
        return repository.recordPayment(params)
    }
}
