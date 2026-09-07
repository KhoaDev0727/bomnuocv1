package com.vn.bomnuocv1.presentation.pumplog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.model.PumpTransaction
import com.vn.bomnuocv1.domain.usecase.DeletePumpTransactionUseCase
import com.vn.bomnuocv1.domain.usecase.GetActivePricingRulesUseCase
import com.vn.bomnuocv1.domain.usecase.GetFarmersUseCase
import com.vn.bomnuocv1.domain.usecase.GetPumpTransactionsUseCase
import com.vn.bomnuocv1.domain.usecase.RecordPaymentUseCase
import com.vn.bomnuocv1.domain.usecase.RecordPumpTransactionUseCase
import com.vn.bomnuocv1.domain.usecase.UpdatePumpTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PumpLogViewModel @Inject constructor(
    private val getPumpTransactionsUseCase: GetPumpTransactionsUseCase,
    private val recordPumpTransactionUseCase: RecordPumpTransactionUseCase,
    private val updatePumpTransactionUseCase: UpdatePumpTransactionUseCase,
    private val deletePumpTransactionUseCase: DeletePumpTransactionUseCase,
    private val recordPaymentUseCase: RecordPaymentUseCase,
    private val getFarmersUseCase: GetFarmersUseCase,
    private val getActivePricingRulesUseCase: GetActivePricingRulesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PumpLogUiState(isLoading = true))
    val uiState: StateFlow<PumpLogUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadData()
    }

    fun loadData(keyword: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val txResult = getPumpTransactionsUseCase(keyword = keyword)
            val farmersResult = getFarmersUseCase()
            val pricingResult = getActivePricingRulesUseCase()

            txResult.onSuccess { txList ->
                _uiState.update {
                    it.copy(
                        transactions = txList,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Không thể tải danh sách lượt bơm."
                    )
                }
            }

            farmersResult.onSuccess { fList ->
                _uiState.update { it.copy(farmers = fList) }
            }

            pricingResult.onSuccess { pList ->
                _uiState.update { it.copy(activePricingRules = pList) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            loadData(query.trim().ifEmpty { null })
        }
    }

    fun onClearSearch() {
        _uiState.update { it.copy(searchQuery = "") }
        searchJob?.cancel()
        loadData(null)
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // --- Record Dialog ---
    fun onOpenRecordDialog(preselectedFarmerId: String? = null) {
        viewModelScope.launch {
            // Always fetch the freshest pricing rules and farmers from repository
            val pricingResult = getActivePricingRulesUseCase()
            val farmersResult = getFarmersUseCase()

            val latestRules = pricingResult.getOrNull() ?: _uiState.value.activePricingRules
            val latestFarmers = farmersResult.getOrNull() ?: _uiState.value.farmers
            val defaultRule = latestRules.firstOrNull()

            _uiState.update {
                it.copy(
                    showRecordDialog = true,
                    activePricingRules = latestRules,
                    farmers = latestFarmers,
                    formFarmerId = preselectedFarmerId ?: latestFarmers.firstOrNull()?.id.orEmpty(),
                    formPricingRuleId = defaultRule?.id,
                    formTransactionDate = getTodayDateString(),
                    formQuantity = "",
                    formQuantityUnit = defaultRule?.unitLabel ?: "công nhỏ (1.000m²)",
                    formUnitPrice = defaultRule?.unitPrice?.toPlainString() ?: "90000",
                    formInitialPaidAmount = "0",
                    formNote = "",
                    formError = null
                )
            }
        }
    }

    fun onDismissRecordDialog() {
        _uiState.update { it.copy(showRecordDialog = false, formError = null) }
    }

    // --- Edit Dialog ---
    fun onOpenEditDialog(transaction: PumpTransaction) {
        _uiState.update {
            it.copy(
                showEditDialog = true,
                selectedTransaction = transaction,
                formFarmerId = transaction.farmerId,
                formPricingRuleId = transaction.pricingRuleId,
                formTransactionDate = transaction.transactionDate,
                formQuantity = transaction.quantity.toPlainString(),
                formQuantityUnit = transaction.quantityUnit,
                formUnitPrice = transaction.unitPrice.toPlainString(),
                formNote = transaction.note.orEmpty(),
                formError = null
            )
        }
    }

    fun onDismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false, selectedTransaction = null, formError = null) }
    }

    // --- Delete Dialog ---
    fun onOpenDeleteConfirmDialog(transaction: PumpTransaction) {
        _uiState.update {
            it.copy(
                showDeleteConfirmDialog = true,
                selectedTransaction = transaction
            )
        }
    }

    fun onDismissDeleteConfirmDialog() {
        _uiState.update { it.copy(showDeleteConfirmDialog = false, selectedTransaction = null) }
    }

    // --- Payment Dialog ---
    fun onOpenPaymentDialog(transaction: PumpTransaction? = null, farmerId: String? = null) {
        val targetFarmerId = transaction?.farmerId ?: farmerId ?: _uiState.value.farmers.firstOrNull()?.id.orEmpty()
        val defaultAmount = if (transaction != null && transaction.remainingDebt > BigDecimal.ZERO) {
            transaction.remainingDebt.toPlainString()
        } else ""

        _uiState.update {
            it.copy(
                showPaymentDialog = true,
                selectedTransaction = transaction,
                paymentFarmerId = targetFarmerId,
                paymentTransactionId = transaction?.id,
                paymentAmount = defaultAmount,
                paymentDate = getTodayDateString(),
                paymentNote = if (transaction != null) "Thanh toán đợt bơm ngày ${transaction.transactionDate}" else "Thanh toán trả nợ",
                formError = null
            )
        }
    }

    fun onDismissPaymentDialog() {
        _uiState.update {
            it.copy(
                showPaymentDialog = false,
                selectedTransaction = null,
                paymentFarmerId = null,
                paymentTransactionId = null,
                paymentAmount = "",
                paymentDate = "",
                paymentNote = "",
                formError = null
            )
        }
    }

    // --- Form Field Updaters ---
    fun onFormFarmerIdChanged(farmerId: String) {
        _uiState.update { it.copy(formFarmerId = farmerId) }
    }

    fun onFormPricingRuleSelected(rule: PricingRule) {
        _uiState.update {
            it.copy(
                formPricingRuleId = rule.id,
                formQuantityUnit = rule.unitLabel,
                formUnitPrice = rule.unitPrice.toPlainString()
            )
        }
    }

    fun onFormQuantityChanged(qty: String) {
        _uiState.update { it.copy(formQuantity = qty, formError = null) }
    }

    fun onFormQuantityUnitChanged(unit: String) {
        _uiState.update { it.copy(formQuantityUnit = unit) }
    }

    fun onFormUnitPriceChanged(price: String) {
        _uiState.update { it.copy(formUnitPrice = price, formError = null) }
    }

    fun onFormInitialPaidChanged(amount: String) {
        _uiState.update { it.copy(formInitialPaidAmount = amount, formError = null) }
    }

    fun onFormTransactionDateChanged(date: String) {
        _uiState.update { it.copy(formTransactionDate = date) }
    }

    fun onFormNoteChanged(note: String) {
        _uiState.update { it.copy(formNote = note) }
    }

    fun onPaymentFarmerIdChanged(farmerId: String) {
        _uiState.update { it.copy(paymentFarmerId = farmerId) }
    }

    fun onPaymentAmountChanged(amount: String) {
        _uiState.update { it.copy(paymentAmount = amount, formError = null) }
    }

    fun onPaymentDateChanged(date: String) {
        _uiState.update { it.copy(paymentDate = date) }
    }

    fun onPaymentNoteChanged(note: String) {
        _uiState.update { it.copy(paymentNote = note) }
    }

    // --- Action Executions ---
    fun onSavePumpTransaction() {
        val state = _uiState.value
        val farmerId = state.formFarmerId.trim()
        if (farmerId.isEmpty()) {
            _uiState.update { it.copy(formError = "Vui lòng chọn nông dân.") }
            return
        }

        val qty = state.formQuantity.trim().toBigDecimalOrNull()
        if (qty == null || qty <= BigDecimal.ZERO) {
            _uiState.update { it.copy(formError = "Diện tích hoặc số giờ bơm phải lớn hơn 0.") }
            return
        }

        val unitPrice = state.formUnitPrice.trim().toBigDecimalOrNull()
        if (unitPrice == null || unitPrice < BigDecimal.ZERO) {
            _uiState.update { it.copy(formError = "Đơn giá không hợp lệ.") }
            return
        }

        val initialPaid = state.formInitialPaidAmount.trim().toBigDecimalOrNull()
        if (initialPaid == null || initialPaid < BigDecimal.ZERO) {
            _uiState.update { it.copy(formError = "Số tiền đã nhận không được âm.") }
            return
        }

        val date = state.formTransactionDate.trim().ifEmpty { getTodayDateString() }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }

            val result = recordPumpTransactionUseCase(
                farmerId = farmerId,
                pricingRuleId = state.formPricingRuleId,
                transactionDate = date,
                quantity = qty,
                quantityUnit = state.formQuantityUnit.trim().ifEmpty { "công nhỏ" },
                unitPrice = unitPrice,
                initialPaidAmount = initialPaid,
                note = state.formNote.trim().ifEmpty { null }
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        showRecordDialog = false,
                        successMessage = "Ghi lượt bơm mới thành công!"
                    )
                }
                loadData(state.searchQuery.ifBlank { null })
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        formError = error.message ?: "Không thể ghi lượt bơm mới."
                    )
                }
            }
        }
    }

    fun onUpdatePumpTransaction() {
        val state = _uiState.value
        val target = state.selectedTransaction ?: return

        val farmerId = state.formFarmerId.trim()
        if (farmerId.isEmpty()) {
            _uiState.update { it.copy(formError = "Vui lòng chọn nông dân.") }
            return
        }

        val qty = state.formQuantity.trim().toBigDecimalOrNull()
        if (qty == null || qty <= BigDecimal.ZERO) {
            _uiState.update { it.copy(formError = "Số lượng phải lớn hơn 0.") }
            return
        }

        val unitPrice = state.formUnitPrice.trim().toBigDecimalOrNull()
        if (unitPrice == null || unitPrice < BigDecimal.ZERO) {
            _uiState.update { it.copy(formError = "Đơn giá không hợp lệ.") }
            return
        }

        val date = state.formTransactionDate.trim().ifEmpty { target.transactionDate }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }

            val result = updatePumpTransactionUseCase(
                id = target.id,
                farmerId = farmerId,
                pricingRuleId = state.formPricingRuleId,
                transactionDate = date,
                quantity = qty,
                quantityUnit = state.formQuantityUnit.trim().ifEmpty { target.quantityUnit },
                unitPrice = unitPrice,
                note = state.formNote.trim().ifEmpty { null }
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        showEditDialog = false,
                        selectedTransaction = null,
                        successMessage = "Cập nhật lượt bơm thành công!"
                    )
                }
                loadData(state.searchQuery.ifBlank { null })
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        formError = error.message ?: "Không thể cập nhật lượt bơm."
                    )
                }
            }
        }
    }

    fun onConfirmDeleteTransaction() {
        val target = _uiState.value.selectedTransaction ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }

            val result = deletePumpTransactionUseCase(target.id)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        showDeleteConfirmDialog = false,
                        selectedTransaction = null,
                        successMessage = "Đã xóa lượt bơm thành công!"
                    )
                }
                loadData(_uiState.value.searchQuery.ifBlank { null })
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = error.message ?: "Không thể xóa lượt bơm."
                    )
                }
            }
        }
    }

    fun onSavePayment() {
        val state = _uiState.value
        val farmerId = state.paymentFarmerId?.trim().orEmpty()
        if (farmerId.isEmpty()) {
            _uiState.update { it.copy(formError = "Vui lòng chọn nông dân nộp tiền.") }
            return
        }

        val amount = state.paymentAmount.trim().toBigDecimalOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
            _uiState.update { it.copy(formError = "Số tiền thanh toán phải lớn hơn 0.") }
            return
        }

        val date = state.paymentDate.trim().ifEmpty { getTodayDateString() }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, formError = null) }

            val result = recordPaymentUseCase(
                farmerId = farmerId,
                transactionId = state.paymentTransactionId,
                amount = amount,
                paymentDate = date,
                note = state.paymentNote.trim().ifEmpty { null }
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        showPaymentDialog = false,
                        selectedTransaction = null,
                        paymentFarmerId = null,
                        paymentTransactionId = null,
                        paymentAmount = "",
                        paymentDate = "",
                        paymentNote = "",
                        successMessage = "Ghi nhận thanh toán thành công!"
                    )
                }
                loadData(state.searchQuery.ifBlank { null })
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        formError = error.message ?: "Không thể ghi nhận thanh toán."
                    )
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}
