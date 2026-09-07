package com.vn.bomnuocv1.presentation.pumplog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vn.bomnuocv1.domain.model.PaymentStatus
import com.vn.bomnuocv1.domain.model.PumpTransaction
import com.vn.bomnuocv1.presentation.common.AppBottomNavigationBar
import com.vn.bomnuocv1.presentation.common.clearFocusOnTap
import com.vn.bomnuocv1.presentation.home.HomeBottomTab
import com.vn.bomnuocv1.presentation.pumplog.components.DeletePumpConfirmDialog
import com.vn.bomnuocv1.presentation.pumplog.components.EditPumpDialog
import com.vn.bomnuocv1.presentation.pumplog.components.RecordPaymentDialog
import com.vn.bomnuocv1.presentation.pumplog.components.RecordPumpDialog
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

@Composable
fun PumpLogScreen(
    onTabSelected: (HomeBottomTab) -> Unit,
    viewModel: PumpLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val decimalSymbols = remember { DecimalFormatSymbols(Locale.GERMAN) }
    val currencyFormatter = remember { DecimalFormat("#,###", decimalSymbols) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onOpenRecordDialog() },
                containerColor = AgriGreenPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ghi lượt bơm mới",
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        bottomBar = {
            AppBottomNavigationBar(
                selectedTab = HomeBottomTab.PUMP_LOG,
                onTabSelected = onTabSelected
            )
        },
        containerColor = AgriBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AgriBackground)
                .clearFocusOnTap()
        ) {
            // 1. Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Nhật ký lượt bơm",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Theo dõi và ghi nhận các đợt bơm nước",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AgriMintContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AgriMintBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AgriGreenPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${uiState.transactions.size} lượt",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }
                }
            }

            HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.6f), thickness = 1.dp)

            // 2. Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = {
                        Text(
                            text = "Tìm theo tên hoặc số điện thoại...",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onClearSearch() }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Xóa tìm kiếm",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
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

            // 3. Summary Metrics Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Tổng tiền tạm tính", fontSize = 11.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${currencyFormatter.format(uiState.totalAmountDue)} đ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Còn nợ chưa thu", fontSize = 11.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${currencyFormatter.format(uiState.totalRemainingDebt)} đ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.totalRemainingDebt > BigDecimal.ZERO) Color(0xFFDC2626) else AgriGreenPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Content List
            if (uiState.isLoading && uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AgriGreenPrimary)
                }
            } else if (uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = AgriMintContainer,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = AgriGreenPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (uiState.searchQuery.isNotEmpty()) "Không tìm thấy lượt bơm phù hợp" else "Chưa có lượt bơm nào được ghi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Nhấn nút + bên dưới để ghi lượt bơm đầu tiên",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.transactions, key = { it.id }) { tx ->
                        PumpTransactionCard(
                            transaction = tx,
                            currencyFormatter = currencyFormatter,
                            onPayClick = { viewModel.onOpenPaymentDialog(transaction = tx) },
                            onEditClick = { viewModel.onOpenEditDialog(tx) },
                            onDeleteClick = { viewModel.onOpenDeleteConfirmDialog(tx) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // --- Dialogs ---
    if (uiState.showRecordDialog) {
        RecordPumpDialog(
            farmers = uiState.farmers,
            pricingRules = uiState.activePricingRules,
            selectedFarmerId = uiState.formFarmerId,
            selectedPricingRuleId = uiState.formPricingRuleId,
            transactionDate = uiState.formTransactionDate,
            quantity = uiState.formQuantity,
            quantityUnit = uiState.formQuantityUnit,
            unitPrice = uiState.formUnitPrice,
            initialPaidAmount = uiState.formInitialPaidAmount,
            note = uiState.formNote,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onFarmerSelected = { viewModel.onFormFarmerIdChanged(it) },
            onPricingRuleSelected = { viewModel.onFormPricingRuleSelected(it) },
            onQuantityChanged = { viewModel.onFormQuantityChanged(it) },
            onQuantityUnitChanged = { viewModel.onFormQuantityUnitChanged(it) },
            onUnitPriceChanged = { viewModel.onFormUnitPriceChanged(it) },
            onInitialPaidChanged = { viewModel.onFormInitialPaidChanged(it) },
            onTransactionDateChanged = { viewModel.onFormTransactionDateChanged(it) },
            onNoteChanged = { viewModel.onFormNoteChanged(it) },
            onDismiss = { viewModel.onDismissRecordDialog() },
            onSave = { viewModel.onSavePumpTransaction() }
        )
    }

    if (uiState.showEditDialog && uiState.selectedTransaction != null) {
        EditPumpDialog(
            farmers = uiState.farmers,
            selectedFarmerId = uiState.formFarmerId,
            transactionDate = uiState.formTransactionDate,
            quantity = uiState.formQuantity,
            quantityUnit = uiState.formQuantityUnit,
            unitPrice = uiState.formUnitPrice,
            note = uiState.formNote,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onFarmerSelected = { viewModel.onFormFarmerIdChanged(it) },
            onQuantityChanged = { viewModel.onFormQuantityChanged(it) },
            onQuantityUnitChanged = { viewModel.onFormQuantityUnitChanged(it) },
            onUnitPriceChanged = { viewModel.onFormUnitPriceChanged(it) },
            onTransactionDateChanged = { viewModel.onFormTransactionDateChanged(it) },
            onNoteChanged = { viewModel.onFormNoteChanged(it) },
            onDismiss = { viewModel.onDismissEditDialog() },
            onSave = { viewModel.onUpdatePumpTransaction() }
        )
    }

    if (uiState.showDeleteConfirmDialog && uiState.selectedTransaction != null) {
        DeletePumpConfirmDialog(
            transaction = uiState.selectedTransaction!!,
            isDeleting = uiState.isDeleting,
            onDismiss = { viewModel.onDismissDeleteConfirmDialog() },
            onConfirm = { viewModel.onConfirmDeleteTransaction() }
        )
    }

    if (uiState.showPaymentDialog) {
        RecordPaymentDialog(
            farmers = uiState.farmers,
            selectedFarmerId = uiState.paymentFarmerId,
            selectedTransaction = uiState.selectedTransaction,
            amount = uiState.paymentAmount,
            paymentDate = uiState.paymentDate,
            note = uiState.paymentNote,
            isSaving = uiState.isSaving,
            errorMessage = uiState.formError,
            onFarmerSelected = { viewModel.onPaymentFarmerIdChanged(it) },
            onAmountChanged = { viewModel.onPaymentAmountChanged(it) },
            onPaymentDateChanged = { viewModel.onPaymentDateChanged(it) },
            onNoteChanged = { viewModel.onPaymentNoteChanged(it) },
            onDismiss = { viewModel.onDismissPaymentDialog() },
            onSave = { viewModel.onSavePayment() }
        )
    }
}

@Composable
private fun PumpTransactionCard(
    transaction: PumpTransaction,
    currencyFormatter: DecimalFormat,
    onPayClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Row 1: Header (Farmer name, Status badge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = CircleShape,
                        color = if (transaction.paymentStatus == PaymentStatus.PAID) AgriMintContainer else Color(0xFFF1F5F9),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = if (transaction.paymentStatus == PaymentStatus.PAID) AgriGreenDark else Color(0xFF0284C7),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = transaction.farmerFullName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!transaction.farmerPhone.isNullOrBlank()) {
                            Text(
                                text = transaction.farmerPhone,
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Status Badge
                when (transaction.paymentStatus) {
                    PaymentStatus.PAID -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AgriMintContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, AgriMintBorder)
                        ) {
                            Text(
                                text = "Đã thu đủ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriGreenDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    PaymentStatus.PARTIAL -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "Trả một phần",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    PaymentStatus.UNPAID -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEE2E2)
                        ) {
                            Text(
                                text = "Chưa thu",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgriError,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 2: Quantity & Unit Price details
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Khối lượng: ${transaction.quantity} ${transaction.quantityUnit}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF334155)
                    )
                    Text(
                        text = "Đơn giá: ${currencyFormatter.format(transaction.unitPrice)}đ",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Amount calculation & remaining debt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ngày bơm: ${transaction.transactionDate}",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    if (transaction.paymentStatus == PaymentStatus.PARTIAL) {
                        Text(
                            text = "Đã trả: ${currencyFormatter.format(transaction.paidAmount)}đ • Còn nợ: ${currencyFormatter.format(transaction.remainingDebt)}đ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFB45309)
                        )
                    } else if (transaction.paymentStatus == PaymentStatus.UNPAID) {
                        Text(
                            text = "Còn nợ: ${currencyFormatter.format(transaction.remainingDebt)}đ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFDC2626)
                        )
                    }
                }

                Text(
                    text = "${currencyFormatter.format(transaction.amountDue)} đ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgriGreenPrimary
                )
            }

            if (!transaction.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ghi chú: ${transaction.note}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.6f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(4.dp))

            // Row 4: Action Buttons (Thu tiền, Sửa, Xóa)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Sửa",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Xóa",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (transaction.remainingDebt > BigDecimal.ZERO) {
                    Button(
                        onClick = onPayClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgriMintContainer,
                            contentColor = AgriGreenDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = AgriGreenDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Thu tiền",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
