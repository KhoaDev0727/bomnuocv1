package com.vn.bomnuocv1.presentation.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.domain.model.PricingType
import com.vn.bomnuocv1.domain.usecase.DeletePricingRuleUseCase
import com.vn.bomnuocv1.domain.usecase.GetActivePricingRulesUseCase
import com.vn.bomnuocv1.domain.usecase.GetAllPricingRulesUseCase
import com.vn.bomnuocv1.domain.usecase.GetLandUnitOptionsUseCase
import com.vn.bomnuocv1.domain.usecase.SavePricingRuleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PricingViewModel @Inject constructor(
    private val getActivePricingRulesUseCase: GetActivePricingRulesUseCase,
    private val getAllPricingRulesUseCase: GetAllPricingRulesUseCase,
    private val getLandUnitOptionsUseCase: GetLandUnitOptionsUseCase,
    private val savePricingRuleUseCase: SavePricingRuleUseCase,
    private val deletePricingRuleUseCase: DeletePricingRuleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PricingUiState(isLoading = true))
    val uiState: StateFlow<PricingUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val unitOptionsRes = getLandUnitOptionsUseCase()
            val activeRulesRes = getActivePricingRulesUseCase()
            val allRulesRes = getAllPricingRulesUseCase()

            val units = unitOptionsRes.getOrDefault(getDefaultUnitOptions())
            val activeRules = activeRulesRes.getOrDefault(emptyList())
            val allRules = allRulesRes.getOrDefault(emptyList())

            val defaultTestUnit = units.firstOrNull()

            _uiState.update {
                it.copy(
                    unitOptions = units,
                    activeRules = activeRules,
                    allRules = allRules,
                    selectedTestUnit = defaultTestUnit,
                    isLoading = false
                )
            }

            recalculateTest()
        }
    }

    fun onTestQuantityChanged(newQuantity: String) {
        _uiState.update { it.copy(testQuantity = newQuantity) }
        recalculateTest()
    }

    fun onTestUnitSelected(unit: LandUnitOption) {
        _uiState.update { it.copy(selectedTestUnit = unit) }
        recalculateTest()
    }

    private fun recalculateTest() {
        val qtyStr = _uiState.value.testQuantity.replace(",", ".").trim()
        val qty = qtyStr.toBigDecimalOrNull() ?: BigDecimal.ZERO
        val selectedUnit = _uiState.value.selectedTestUnit

        // Find active price for this unit or fallback to default price
        val activeRule = _uiState.value.activeRules.firstOrNull { it.unitLabel == selectedUnit?.label }
        val price = activeRule?.unitPrice ?: selectedUnit?.defaultPrice ?: BigDecimal("90000")

        val total = qty.multiply(price)
        val symbols = DecimalFormatSymbols(Locale.GERMAN)
        val formatter = DecimalFormat("#,###", symbols)
        val formatted = formatter.format(total) + " đ"

        val sqmMeters = selectedUnit?.squareMeters ?: BigDecimal.ZERO
        val totalM2 = if (sqmMeters > BigDecimal.ZERO) {
            val totalM2Value = qty.multiply(sqmMeters)
            "${formatter.format(totalM2Value)} m²"
        } else {
            ""
        }

        _uiState.update {
            it.copy(
                testTotalAmount = total,
                formattedTestTotal = formatted,
                testAreaEquivalentM2 = totalM2
            )
        }
    }

    fun openEditDialog(rule: PricingRule? = null, prefillUnit: LandUnitOption? = null) {
        val type = rule?.pricingType?.code ?: (if (prefillUnit?.code == "HOUR") "per_hour" else "per_area")
        val label = rule?.unitLabel ?: (prefillUnit?.label ?: "công nhỏ (1.000m²)")
        val price = rule?.unitPrice?.toPlainString() ?: (prefillUnit?.defaultPrice?.toPlainString() ?: "90000")

        _uiState.update {
            it.copy(
                showEditDialog = true,
                editingPricingType = type,
                editingUnitLabel = label,
                editingPriceInput = price,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun dismissEditDialog() {
        _uiState.update { it.copy(showEditDialog = false) }
    }

    fun onPriceInputChanged(newPrice: String) {
        val digitsOnly = newPrice.filter { it.isDigit() }
        _uiState.update { it.copy(editingPriceInput = digitsOnly) }
    }

    fun onUnitLabelSelected(unitLabel: String) {
        val type = if (unitLabel.contains("giờ", ignoreCase = true)) "per_hour" else "per_area"
        _uiState.update {
            it.copy(
                editingUnitLabel = unitLabel,
                editingPricingType = type
            )
        }
    }

    fun setQuickPricePreset(presetAmount: Long) {
        _uiState.update { it.copy(editingPriceInput = presetAmount.toString()) }
    }

    fun addQuickPriceStep(stepAmount: Long) {
        val current = _uiState.value.editingPriceInput.toLongOrNull() ?: 0L
        val newPrice = (current + stepAmount).coerceAtLeast(0)
        _uiState.update { it.copy(editingPriceInput = newPrice.toString()) }
    }

    fun savePricingRule() {
        val price = _uiState.value.editingPriceInput.toBigDecimalOrNull()
        if (price == null || price < BigDecimal.ZERO) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đơn giá hợp lệ.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val result = savePricingRuleUseCase(
                pricingType = _uiState.value.editingPricingType,
                unitLabel = _uiState.value.editingUnitLabel,
                unitPrice = price
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        showEditDialog = false,
                        successMessage = "Thiết lập đơn giá thành công!"
                    )
                }
                loadData()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "Không thể lưu đơn giá."
                    )
                }
            }
        }
    }

    fun requestDeletePricingRule(rule: PricingRule) {
        _uiState.update { it.copy(rulePendingDelete = rule) }
    }

    fun dismissDeleteConfirmation() {
        _uiState.update { it.copy(rulePendingDelete = null) }
    }

    fun confirmDeletePricingRule() {
        val rule = _uiState.value.rulePendingDelete ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, errorMessage = null) }
            val result = deletePricingRuleUseCase(rule.id)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        rulePendingDelete = null,
                        successMessage = "Đã xóa đơn giá '${rule.unitLabel}' thành công!"
                    )
                }
                loadData()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        rulePendingDelete = null,
                        errorMessage = error.message ?: "Không thể xóa đơn giá."
                    )
                }
            }
        }
    }

    fun toggleLandUnitGuide() {
        _uiState.update { it.copy(isLandUnitGuideExpanded = !it.isLandUnitGuideExpanded) }
    }

    fun toggleHistoryExpanded() {
        _uiState.update { it.copy(isHistoryExpanded = !it.isHistoryExpanded) }
    }

    fun toggleActiveRulesExpanded() {
        _uiState.update { it.copy(isActiveRulesExpanded = !it.isActiveRulesExpanded) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    private fun getDefaultUnitOptions(): List<LandUnitOption> {
        return listOf(
            LandUnitOption("CONG_NHO_1000", "công nhỏ (1.000m²)", "Công tầm nhỏ", BigDecimal("1000"), BigDecimal("90000"), "90.000 đ", "Chuẩn Nam Bộ / 1.000 mét vuông"),
            LandUnitOption("CONG_LON_1296", "công lớn (1.296m²)", "Công tầm lớn (3m)", BigDecimal("1296"), BigDecimal("115000"), "115.000 đ", "Tây Nam Bộ / 144 tầm vuông (tầm 3m) = 1.296 m²"),
            LandUnitOption("CONG_LON_1440", "công lớn (1.440m²)", "Công tầm lớn (3m25)", BigDecimal("1440"), BigDecimal("130000"), "130.000 đ", "Tây Nam Bộ / tầm cắt 3m25 = ~1.440 m²"),
            LandUnitOption("SAO_BAC_BO_360", "sào Bắc Bộ (360m²)", "Sào Bắc Bộ", BigDecimal("360"), BigDecimal("35000"), "35.000 đ", "Bắc Bộ / 360 mét vuông"),
            LandUnitOption("SAO_TRUNG_BO_500", "sào Trung Bộ (500m²)", "Sào Trung Bộ", BigDecimal("500"), BigDecimal("45000"), "45.000 đ", "Trung Bộ / 500 mét vuông"),
            LandUnitOption("HECTA_10000", "hecta (10.000m²)", "Hecta (ha)", BigDecimal("10000"), BigDecimal("900000"), "900.000 đ", "1 Hecta = 10.000 mét vuông (10 công nhỏ)"),
            LandUnitOption("HOUR", "giờ", "Theo giờ bơm", BigDecimal.ZERO, BigDecimal("60000"), "60.000 đ", "Tính tiền theo thời gian bơm nước")
        )
    }
}
