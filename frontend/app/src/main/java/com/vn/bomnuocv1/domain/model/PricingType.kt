package com.vn.bomnuocv1.domain.model

enum class PricingType(val code: String, val description: String) {
    PER_AREA("per_area", "Theo diện tích"),
    PER_HOUR("per_hour", "Theo thời gian (giờ)");

    companion object {
        fun fromCode(code: String?): PricingType {
            if (code == null) return PER_AREA
            return entries.firstOrNull {
                it.code.equals(code.trim(), ignoreCase = true) ||
                        it.name.equals(code.trim(), ignoreCase = true)
            } ?: PER_AREA
        }
    }
}
