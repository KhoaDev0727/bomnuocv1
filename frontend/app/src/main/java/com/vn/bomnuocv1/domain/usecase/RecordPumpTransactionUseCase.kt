package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.PumpTransaction
import com.vn.bomnuocv1.domain.repository.CreatePumpTransactionParams
import com.vn.bomnuocv1.domain.repository.PumpTransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

class RecordPumpTransactionUseCase @Inject constructor(
    private val repository: PumpTransactionRepository
) {
    suspend operator fun invoke(
        farmerId: String,
        pricingRuleId: String?,
        transactionDate: String,
        quantity: BigDecimal,
        quantityUnit: String,
        unitPrice: BigDecimal,
        initialPaidAmount: BigDecimal?,
        note: String?
    ): Result<PumpTransaction> {
        val params = CreatePumpTransactionParams(
            farmerId = farmerId,
            pricingRuleId = pricingRuleId,
            transactionDate = transactionDate,
            quantity = quantity,
            quantityUnit = quantityUnit,
            unitPrice = unitPrice,
            initialPaidAmount = initialPaidAmount,
            note = note
        )
        return repository.createTransaction(params)
    }
}
