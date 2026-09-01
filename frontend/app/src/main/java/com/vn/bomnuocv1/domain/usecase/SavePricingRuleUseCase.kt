package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.repository.PricingRepository
import java.math.BigDecimal
import javax.inject.Inject

class SavePricingRuleUseCase @Inject constructor(
    private val repository: PricingRepository
) {
    suspend operator fun invoke(
        pricingType: String,
        unitLabel: String,
        unitPrice: BigDecimal,
        effectiveFrom: String? = null
    ): Result<PricingRule> {
        return repository.savePricingRule(pricingType, unitLabel, unitPrice, effectiveFrom)
    }
}
