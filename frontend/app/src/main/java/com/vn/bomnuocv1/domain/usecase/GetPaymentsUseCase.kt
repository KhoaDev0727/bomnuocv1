package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.Payment
import com.vn.bomnuocv1.domain.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentsUseCase @Inject constructor(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke(
        farmerId: String? = null,
        transactionId: String? = null
    ): Result<List<Payment>> {
        return repository.getPayments(farmerId, transactionId)
    }
}
