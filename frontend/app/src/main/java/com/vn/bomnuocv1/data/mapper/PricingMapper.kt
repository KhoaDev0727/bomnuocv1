package com.vn.bomnuocv1.data.mapper

import com.vn.bomnuocv1.data.remote.dto.LandUnitOptionDto
import com.vn.bomnuocv1.data.remote.dto.PricingRuleResponseDto
import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.model.PricingType
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun PricingRuleResponseDto.toDomain(): PricingRule {
    val symbols = DecimalFormatSymbols(Locale.GERMAN)
    val formatter = DecimalFormat("#,###", symbols)
    val formattedPrice = formattedUnitPrice ?: (formatter.format(unitPrice) + " đ")

    return PricingRule(
        id = id,
        ownerId = ownerId,
        pricingType = PricingType.fromCode(pricingType),
        pricingTypeDescription = pricingTypeDescription ?: (if (pricingType == "per_hour") "Theo thời gian" else "Theo diện tích"),
        unitLabel = unitLabel,
        unitPrice = unitPrice,
        formattedUnitPrice = formattedPrice,
        effectiveFrom = effectiveFrom,
        effectiveTo = effectiveTo,
        active = active,
        createdAt = createdAt
    )
}

fun LandUnitOptionDto.toDomain(): LandUnitOption {
    val symbols = DecimalFormatSymbols(Locale.GERMAN)
    val formatter = DecimalFormat("#,###", symbols)
    val formattedPrice = formattedDefaultPrice ?: (formatter.format(defaultPrice) + " đ")

    return LandUnitOption(
        code = code,
        label = label,
        displayName = displayName,
        squareMeters = squareMeters,
        defaultPrice = defaultPrice,
        formattedDefaultPrice = formattedPrice,
        description = description
    )
}
