package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.PumpTransaction
import com.vn.bomnuocv1.domain.repository.PumpTransactionRepository
import com.vn.bomnuocv1.domain.repository.UpdatePumpTransactionParams
import java.math.BigDecimal
import javax.inject.Inject

class UpdatePumpTransactionUseCase @Inject constructor(
    private val repository: PumpTransactionRepository
) {
    suspend operator fun invoke(
        id: String,
        farmerId: String,
        pricingRuleId: String?,
        transactionDate: String,
        quantity: BigDecimal,
        quantityUnit: String,
        unitPrice: BigDecimal,
        note: String?
    ): Result<PumpTransaction> {
        val params = UpdatePumpTransactionParams(
            farmerId = farmerId,
            pricingRuleId = pricingRuleId,
            transactionDate = transactionDate,
            quantity = quantity,
            quantityUnit = quantityUnit,
            unitPrice = unitPrice,
            note = note
        )
        return repository.updateTransaction(id, params)
    }
}
