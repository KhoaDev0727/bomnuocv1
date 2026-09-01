package com.vn.bomnuocv1.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PricingRuleResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("pricingType") val pricingType: String,
    @SerializedName("pricingTypeDescription") val pricingTypeDescription: String?,
    @SerializedName("unitLabel") val unitLabel: String,
    @SerializedName("unitPrice") val unitPrice: BigDecimal,
    @SerializedName("formattedUnitPrice") val formattedUnitPrice: String?,
    @SerializedName("effectiveFrom") val effectiveFrom: String,
    @SerializedName("effectiveTo") val effectiveTo: String?,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: String?
)

data class SavePricingRuleRequestDto(
    @SerializedName("pricingType") val pricingType: String,
    @SerializedName("unitLabel") val unitLabel: String,
    @SerializedName("unitPrice") val unitPrice: BigDecimal,
    @SerializedName("effectiveFrom") val effectiveFrom: String? = null
)

data class LandUnitOptionDto(
    @SerializedName("code") val code: String,
    @SerializedName("label") val label: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("squareMeters") val squareMeters: BigDecimal,
    @SerializedName("defaultPrice") val defaultPrice: BigDecimal,
    @SerializedName("formattedDefaultPrice") val formattedDefaultPrice: String?,
    @SerializedName("description") val description: String
)
