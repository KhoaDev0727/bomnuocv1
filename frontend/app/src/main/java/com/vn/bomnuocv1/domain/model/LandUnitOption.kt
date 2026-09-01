package com.vn.bomnuocv1.domain.model

import java.math.BigDecimal

data class LandUnitOption(
    val code: String,
    val label: String,
    val displayName: String,
    val squareMeters: BigDecimal,
    val defaultPrice: BigDecimal,
    val formattedDefaultPrice: String,
    val description: String
)
