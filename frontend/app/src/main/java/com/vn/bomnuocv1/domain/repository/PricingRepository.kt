package com.vn.bomnuocv1.domain.repository

import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.model.PricingRule
import java.math.BigDecimal

interface PricingRepository {

    suspend fun getActivePricingRules(): Result<List<PricingRule>>

    suspend fun getAllPricingRules(): Result<List<PricingRule>>

    suspend fun savePricingRule(
        pricingType: String,
        unitLabel: String,
        unitPrice: BigDecimal,
        effectiveFrom: String?
    ): Result<PricingRule>

    suspend fun getLandUnitOptions(): Result<List<LandUnitOption>>
}
