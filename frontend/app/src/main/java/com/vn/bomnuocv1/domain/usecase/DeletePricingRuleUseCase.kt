package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.repository.PricingRepository
import javax.inject.Inject

class DeletePricingRuleUseCase @Inject constructor(
    private val repository: PricingRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deletePricingRule(id)
    }
}
