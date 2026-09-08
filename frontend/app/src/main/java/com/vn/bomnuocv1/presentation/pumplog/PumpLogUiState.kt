package com.vn.bomnuocv1.presentation.pumplog

import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.model.PumpTransaction
import java.math.BigDecimal

data class PumpLogUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val transactions: List<PumpTransaction> = emptyList(),
    val farmers: List<Farmer> = emptyList(),
    val activePricingRules: List<PricingRule> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val successMessage: String? = null,

    // Dialog visibility states
    val showRecordDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false,
    val showPaymentDialog: Boolean = false,

    // Target transaction for edit / delete / payment
    val selectedTransaction: PumpTransaction? = null,

    // Target farmer for payment dialog
    val paymentFarmerId: String? = null,
    val paymentTransactionId: String? = null,
    val paymentAmount: String = "",
    val paymentDate: String = "",
    val paymentNote: String = "",

    // Form fields for Record / Edit
    val formFarmerId: String = "",
    val formPricingRuleId: String? = null,
    val formTransactionDate: String = "",
    val formQuantity: String = "",
    val formQuantityUnit: String = "",
    val formUnitPrice: String = "",
    val formInitialPaidAmount: String = "0",
    val formNote: String = "",
    val formError: String? = null
) {
    val totalAmountDue: BigDecimal
        get() = transactions.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.amountDue) }

    val totalPaidAmount: BigDecimal
        get() = transactions.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.paidAmount) }

    val totalRemainingDebt: BigDecimal
        get() = transactions.fold(BigDecimal.ZERO) { acc, t -> acc.add(t.remainingDebt) }
}
