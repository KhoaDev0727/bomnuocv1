package com.vn.bomnuocv1.domain.model

import java.math.BigDecimal

data class PricingRule(
    val id: String,
    val ownerId: String,
    val pricingType: PricingType,
    val pricingTypeDescription: String,
    val unitLabel: String,
    val unitPrice: BigDecimal,
    val formattedUnitPrice: String,
    val effectiveFrom: String,
    val effectiveTo: String?,
    val active: Boolean,
    val createdAt: String?
)
