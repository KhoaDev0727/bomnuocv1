package com.vn.bomnuocv1.domain.usecase

import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.repository.PricingRepository
import javax.inject.Inject

class GetAllPricingRulesUseCase @Inject constructor(
    private val repository: PricingRepository
) {
    suspend operator fun invoke(): Result<List<PricingRule>> {
        return repository.getAllPricingRules()
    }
}
