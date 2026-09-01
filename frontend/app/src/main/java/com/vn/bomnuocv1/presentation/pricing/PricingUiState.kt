package com.vn.bomnuocv1.presentation.pricing

import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.model.PricingRule
import java.math.BigDecimal

data class PricingUiState(
    val activeRules: List<PricingRule> = emptyList(),
    val allRules: List<PricingRule> = emptyList(),
    val unitOptions: List<LandUnitOption> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    // Calculator tool state
    val testQuantity: String = "2.5",
    val selectedTestUnit: LandUnitOption? = null,
    val testTotalAmount: BigDecimal = BigDecimal.ZERO,
    val formattedTestTotal: String = "0 đ",
    val testAreaEquivalentM2: String = "",

    // Dialog state for adding/editing rule
    val showEditDialog: Boolean = false,
    val editingPricingType: String = "per_area",
    val editingUnitLabel: String = "công nhỏ (1.000m²)",
    val editingPriceInput: String = "90000"
)
