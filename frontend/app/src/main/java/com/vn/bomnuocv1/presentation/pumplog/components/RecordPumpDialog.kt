package com.vn.bomnuocv1.presentation.pumplog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vn.bomnuocv1.domain.model.Farmer
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriError
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriMintBorder
import com.vn.bomnuocv1.ui.theme.AgriMintContainer
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

import androidx.compose.material.icons.filled.CalendarToday
import com.vn.bomnuocv1.presentation.common.AgriDatePickerDialog
import com.vn.bomnuocv1.presentation.common.clearFocusOnTap
import com.vn.bomnuocv1.presentation.common.formatIsoToVietnameseDate

@Composable
fun RecordPumpDialog(
    farmers: List<Farmer>,
    pricingRules: List<PricingRule>,
    selectedFarmerId: String,
    selectedPricingRuleId: String?,
    transactionDate: String,
    quantity: String,
    quantityUnit: String,
    unitPrice: String,
    initialPaidAmount: String,
    note: String,
    isSaving: Boolean,
    errorMessage: String?,
    onFarmerSelected: (String) -> Unit,
    onPricingRuleSelected: (PricingRule) -> Unit,
    onQuantityChanged: (String) -> Unit,
    onQuantityUnitChanged: (String) -> Unit,
    onUnitPriceChanged: (String) -> Unit,
    onInitialPaidChanged: (String) -> Unit,
    onTransactionDateChanged: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var farmerDropdownExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val decimalSymbols = remember { DecimalFormatSymbols(Locale.GERMAN) }
    val currencyFormatter = remember { DecimalFormat("#,###", decimalSymbols) }

    // Auto calculate Total amount & Remaining debt
    val qtyVal = quantity.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
    val priceVal = unitPrice.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
    val totalAmount = qtyVal.multiply(priceVal)

    val paidVal = initialPaidAmount.trim().toBigDecimalOrNull() ?: BigDecimal.ZERO
    val remainingDebt = if (totalAmount > paidVal) totalAmount.subtract(paidVal) else BigDecimal.ZERO

    if (showDatePicker) {
        AgriDatePickerDialog(
            initialIsoDate = transactionDate,
            onDateSelected = { selectedIso ->
                onTransactionDateChanged(selectedIso)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Dialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.90f)
                .padding(vertical = 8.dp)
                .clearFocusOnTap(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clearFocusOnTap()
            ) {
                // Scrollable Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ghi lượt bơm mới",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Tính tiền tự động theo đơn giá đã cài",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    IconButton(onClick = onDismiss, enabled = !isSaving) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                HorizontalDivider(
                    color = AgriCardBorder.copy(alpha = 0.7f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // 1. Select Farmer
                Text(
                    text = "Nông dân nhận bơm *",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(6.dp))

                val selectedFarmer = farmers.find { it.id == selectedFarmerId }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { farmerDropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedFarmer?.fullName ?: "Chọn nông dân...",
                                color = if (selectedFarmer != null) Color(0xFF0F172A) else Color(0xFF94A3B8),
                                fontSize = 14.sp,
                                fontWeight = if (selectedFarmer != null) FontWeight.SemiBold else FontWeight.Normal
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF64748B)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = farmerDropdownExpanded,
                        onDismissRequest = { farmerDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                    ) {
                        if (farmers.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Chưa có nông dân nào", color = Color(0xFF64748B)) },
                                onClick = { farmerDropdownExpanded = false }
                            )
                        } else {
                            farmers.forEach { farmer ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = farmer.fullName,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A),
                                                fontSize = 14.sp
                                            )
                                            val subText = listOfNotNull(
                                                farmer.phoneNumber,
                                                farmer.areaNote
                                            ).joinToString(" • ")
                                            if (subText.isNotEmpty()) {
                                                Text(
                                                    text = subText,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF64748B)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        onFarmerSelected(farmer.id)
                                        farmerDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Select Pricing Rule (Quick Chips)
                if (pricingRules.isNotEmpty()) {
                    Text(
                        text = "Chọn biểu giá áp dụng",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pricingRules.forEach { rule ->
                            val isSelected = selectedPricingRuleId == rule.id
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) AgriMintContainer else Color(0xFFF8FAFC),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) AgriGreenPrimary else AgriCardBorder
                                ),
                                modifier = Modifier.clickable { onPricingRuleSelected(rule) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = AgriGreenPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(
                                        text = "${rule.unitLabel} (${currencyFormatter.format(rule.unitPrice)}đ)",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) AgriGreenDark else Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFFBEB),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Chưa có đơn giá áp dụng. Vui lòng vào mục 'Thiết lập đơn giá' để thêm đơn giá trước khi ghi lượt bơm.",
                                fontSize = 12.sp,
                                color = Color(0xFF92400E),
                                lineHeight = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // 3. Quantity & Unit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "Số lượng *",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = onQuantityChanged,
                            placeholder = { Text("VD: 3.5", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AgriGreenPrimary,
                                unfocusedBorderColor = AgriCardBorder,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Đơn vị tính",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(biểu giá)",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = quantityUnit,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            placeholder = { Text("Chưa có biểu giá", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AgriCardBorder,
                                unfocusedBorderColor = AgriCardBorder,
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedTextColor = Color(0xFF334155),
                                unfocusedTextColor = Color(0xFF334155)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Unit Price & Transaction Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                 text = "Đơn giá (đ)",
                                 fontSize = 13.sp,
                                 fontWeight = FontWeight.SemiBold,
                                 color = Color(0xFF1E293B)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = if (unitPrice.isNotBlank() && priceVal > BigDecimal.ZERO) "${currencyFormatter.format(priceVal)} đ" else "0 đ",
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AgriCardBorder,
                                unfocusedBorderColor = AgriCardBorder,
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedTextColor = if (priceVal > BigDecimal.ZERO) Color(0xFF0F172A) else Color(0xFF94A3B8),
                                unfocusedTextColor = if (priceVal > BigDecimal.ZERO) Color(0xFF0F172A) else Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ngày bơm *",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showDatePicker = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = formatIsoToVietnameseDate(transactionDate),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF0F172A),
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Chọn ngày",
                                    tint = AgriGreenPrimary,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. PROMINENT AUTO-CALCULATED AMOUNT CARD
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = AgriMintContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AgriMintBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "THÀNH TIỀN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark
                            )
                            Text(
                                text = if (priceVal > BigDecimal.ZERO) {
                                    "${quantity.ifEmpty { "0" }} × ${currencyFormatter.format(priceVal)}đ"
                                } else {
                                    "0 × 0đ (Chưa có đơn giá)"
                                },
                                fontSize = 11.sp,
                                color = Color(0xFF475569)
                            )
                        }

                        Text(
                            text = "${currencyFormatter.format(totalAmount)} đ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (totalAmount > BigDecimal.ZERO) AgriGreenPrimary else Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6. INITIAL PAID AMOUNT (Số tiền đã nhận) & REMAINING DEBT (Còn nợ)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Số tiền đã nhận ngay lúc bơm (đ)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = initialPaidAmount,
                        onValueChange = onInitialPaidChanged,
                        placeholder = { Text("0 nếu chưa trả", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgriGreenPrimary,
                            unfocusedBorderColor = AgriCardBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Real-time remaining debt calculation summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Số tiền còn ghi nợ:",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "${currencyFormatter.format(remainingDebt)} đ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (remainingDebt > BigDecimal.ZERO) Color(0xFFDC2626) else AgriGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 7. Note
                Text(
                    text = "Ghi chú (không bắt buộc)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = onNoteChanged,
                    placeholder = { Text("VD: Bơm thêm bờ bao, ruộng sau nhà...", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                    singleLine = false,
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = AgriCardBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Error alert
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEE2E2)
                    ) {
                        Text(
                            text = errorMessage,
                            color = AgriError,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                }

                // Pinned Bottom Button Bar (Always visible inside dialog, never cut off)
                HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.6f), thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        enabled = !isSaving,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(text = "Hủy", color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onSave,
                        enabled = !isSaving,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgriGreenPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isSaving) "Đang lưu..." else "Lưu lượt bơm",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
