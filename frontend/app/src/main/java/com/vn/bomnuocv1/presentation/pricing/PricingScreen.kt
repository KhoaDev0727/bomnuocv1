package com.vn.bomnuocv1.presentation.pricing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vn.bomnuocv1.domain.model.LandUnitOption
import com.vn.bomnuocv1.domain.model.PricingRule
import com.vn.bomnuocv1.presentation.common.AgriButton
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimaryContainer
import com.vn.bomnuocv1.ui.theme.AgriMintBorder
import com.vn.bomnuocv1.ui.theme.AgriMintContainer
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PricingScreen(
    onNavigateBack: () -> Unit,
    viewModel: PricingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    if (uiState.showEditDialog) {
        PricingEditDialog(
            unitOptions = uiState.unitOptions,
            selectedUnitLabel = uiState.editingUnitLabel,
            priceInput = uiState.editingPriceInput,
            isSaving = uiState.isSaving,
            onUnitSelected = viewModel::onUnitLabelSelected,
            onPriceChanged = viewModel::onPriceInputChanged,
            onPresetClick = viewModel::setQuickPricePreset,
            onAddStepClick = viewModel::addQuickPriceStep,
            onDismiss = viewModel::dismissEditDialog,
            onSave = viewModel::savePricingRule
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AgriBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AgriBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color(0xFF0F172A)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column {
                    Text(
                        text = "Thiết lập đơn giá bơm",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Công thức tính tiền & đơn vị đo lường nông nghiệp",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.6f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // 2. Active Pricing Rates Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Đơn giá đang áp dụng",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    TextButton(onClick = { viewModel.openEditDialog() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = AgriGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Thêm đơn giá",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AgriGreenPrimary)
                    }
                } else {
                    val activeList = uiState.activeRules.ifEmpty {
                        listOf(
                            PricingRule("1", "", com.vn.bomnuocv1.domain.model.PricingType.PER_AREA, "Theo diện tích", "công nhỏ (1.000m²)", java.math.BigDecimal("90000"), "90.000 đ", "Hôm nay", null, true, null),
                            PricingRule("2", "", com.vn.bomnuocv1.domain.model.PricingType.PER_AREA, "Theo diện tích", "công lớn (1.296m²)", java.math.BigDecimal("115000"), "115.000 đ", "Hôm nay", null, true, null),
                            PricingRule("3", "", com.vn.bomnuocv1.domain.model.PricingType.PER_HOUR, "Theo thời gian", "giờ", java.math.BigDecimal("60000"), "60.000 đ", "Hôm nay", null, true, null)
                        )
                    }

                    activeList.forEach { rule ->
                        ActivePricingRateCard(
                            rule = rule,
                            onEditClick = { viewModel.openEditDialog(rule) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Interactive Calculation Tool (Công cụ tính thử tiền bơm)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AgriMintContainer.copy(alpha = 0.5f)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriMintBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(AgriGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Công cụ tính thử tiền bơm",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quantity Input
                        OutlinedTextField(
                            value = uiState.testQuantity,
                            onValueChange = viewModel::onTestQuantityChanged,
                            label = { Text("Nhập diện tích đất (hoặc số giờ)") },
                            trailingIcon = {
                                Text(
                                    text = uiState.selectedTestUnit?.displayName ?: "công",
                                    color = Color(0xFF64748B),
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AgriGreenPrimary,
                                focusedLabelColor = AgriGreenPrimary,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Chọn loại công / đơn vị:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Unit selector chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            uiState.unitOptions.forEach { unit ->
                                val isSelected = uiState.selectedTestUnit?.code == unit.code
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) AgriGreenPrimary else Color.White)
                                        .border(
                                            1.dp,
                                            if (isSelected) AgriGreenPrimary else AgriCardBorder,
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable { viewModel.onTestUnitSelected(unit) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = unit.displayName,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF334155)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Live Result Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Thành tiền ước tính:",
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = uiState.formattedTestTotal,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgriGreenPrimary
                                )
                                if (uiState.testAreaEquivalentM2.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Quy đổi diện tích: ${uiState.testAreaEquivalentM2}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF0284C7)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Educational Knowledge Card (Công tầm nhỏ vs Công tầm lớn)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Phân biệt Công tầm nhỏ & Công tầm lớn",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "• Công tầm nhỏ (1.000 m²): Là đơn vị chuẩn đo lường phổ biến ở Nam Bộ.\n" +
                                    "• Công tầm lớn (1.296 m² - 1.440 m²): Dùng phổ biến ở miền Tây (Kiên Giang, An Giang, Cà Mau...), tính theo tầm cấy 3m (144 tầm vuông = 1.296 m²) hoặc tầm 3m25 (~1.440 m²).\n" +
                                    "• Hệ thống tự động nhân diện tích với đơn giá để ghi nợ chính xác, tránh nhầm lẫn.",
                            fontSize = 13.sp,
                            color = Color(0xFF475569),
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 5. Pricing Change History Section
                if (uiState.allRules.isNotEmpty()) {
                    Text(
                        text = "Lịch sử thay đổi đơn giá",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            uiState.allRules.forEachIndexed { index, rule ->
                                PricingHistoryRow(rule = rule)
                                if (index < uiState.allRules.size - 1) {
                                    HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.5f), thickness = 1.dp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ActivePricingRateCard(
    rule: PricingRule,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AgriGreenPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PriceChange,
                        contentDescription = null,
                        tint = AgriGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = rule.unitLabel,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Áp dụng từ: ${rule.effectiveFrom}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = rule.formattedUnitPrice,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgriGreenPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Chỉnh sửa",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PricingHistoryRow(rule: PricingRule) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = rule.unitLabel,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(2.dp))
            val dateText = if (rule.active) "Từ ${rule.effectiveFrom} (Đang áp dụng)" else "Từ ${rule.effectiveFrom} đến ${rule.effectiveTo ?: "nay"}"
            Text(
                text = dateText,
                fontSize = 12.sp,
                color = if (rule.active) AgriGreenPrimary else Color(0xFF64748B)
            )
        }

        Text(
            text = rule.formattedUnitPrice,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (rule.active) AgriGreenPrimary else Color(0xFF64748B)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun PricingEditDialog(
    unitOptions: List<LandUnitOption>,
    selectedUnitLabel: String,
    priceInput: String,
    isSaving: Boolean,
    onUnitSelected: (String) -> Unit,
    onPriceChanged: (String) -> Unit,
    onPresetClick: (Long) -> Unit,
    onAddStepClick: (Long) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var expandedDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Thiết lập đơn giá",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Chọn loại đơn vị tính:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Exposed Dropdown Menu
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedUnitLabel,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgriGreenPrimary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        unitOptions.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text("${unit.displayName} (${unit.label})") },
                                onClick = {
                                    onUnitSelected(unit.label)
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Đơn giá (VNĐ):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = priceInput,
                    onValueChange = onPriceChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    trailingIcon = { Text("đ", color = Color(0xFF64748B), modifier = Modifier.padding(end = 12.dp)) },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        focusedLabelColor = AgriGreenPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick presets
                Text(
                    text = "Chọn nhanh giá phổ biến:",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(80000L, 90000L, 100000L, 115000L, 130000L)
                    presets.forEach { preset ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1F5F9))
                                .clickable { onPresetClick(preset) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${preset / 1000}k",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                        }
                    }

                    // Steps +5k, +10k
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriMintContainer)
                            .clickable { onAddStepClick(5000L) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("+5k", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriMintContainer)
                            .clickable { onAddStepClick(10000L) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("+10k", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                    }
                }
            }
        },
        confirmButton = {
            AgriButton(
                text = "Lưu đơn giá",
                onClick = onSave,
                isLoading = isSaving,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Hủy", color = Color(0xFF64748B))
            }
        }
    )
}
