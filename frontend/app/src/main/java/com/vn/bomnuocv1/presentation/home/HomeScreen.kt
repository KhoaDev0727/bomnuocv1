package com.vn.bomnuocv1.presentation.home

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.PersonAddAlt1
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vn.bomnuocv1.domain.model.RecentTransaction
import com.vn.bomnuocv1.presentation.common.AppBottomNavigationBar
import com.vn.bomnuocv1.presentation.common.OfflineStatusBadge
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimaryContainer
import com.vn.bomnuocv1.ui.theme.AgriMintBorder
import com.vn.bomnuocv1.ui.theme.AgriMintContainer

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToPricing: () -> Unit = {},
    onNavigateToFarmers: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToPumpLog: () -> Unit = {},
    onNavigateToDebtLedger: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppBottomNavigationBar(
                selectedTab = HomeBottomTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        HomeBottomTab.HOME -> { /* Already on Home */ }
                        HomeBottomTab.PUMP_LOG -> onNavigateToPumpLog()
                        HomeBottomTab.DEBT_LEDGER -> onNavigateToDebtLedger()
                        HomeBottomTab.SETTINGS -> onNavigateToSettings()
                    }
                }
            )
        },
        containerColor = AgriBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AgriBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Top Header Bar
            HomeTopBar(
                isOffline = uiState.isOfflineMode,
                onNotificationClick = { /* Handle notifications */ }
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = AgriCardBorder.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // 2. Greeting Header
                val greetingName = uiState.user?.fullName?.ifBlank { "Chủ trạm" } ?: "Chủ trạm"
                Text(
                    text = "Chào ngày mới, $greetingName!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Summary Metric Cards
                // Card 1: Tổng nợ chưa thu
                val debtText = uiState.dashboardSummary?.formattedTotalDebt ?: "15.450.000đ"
                MetricCard(
                    icon = Icons.Outlined.Payments,
                    iconTint = Color(0xFF475569),
                    title = "Tổng nợ chưa thu",
                    value = debtText,
                    valueColor = AgriGreenPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Card 2: Lượt bơm hôm nay
                val pumpCount = uiState.dashboardSummary?.todayPumpCount ?: 12L
                MetricCard(
                    icon = Icons.Outlined.WaterDrop,
                    iconTint = Color(0xFF475569),
                    title = "Lượt bơm hôm nay",
                    value = "$pumpCount",
                    valueSuffix = "lượt",
                    valueColor = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 4. Quick Actions ("Thao tác nhanh")
                Text(
                    text = "Thao tác nhanh",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Action 1: Ghi lượt bơm mới (Primary Filled Card)
                    QuickActionPrimaryCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.AddCircleOutline,
                        text = "Ghi lượt\nbơm mới",
                        onClick = { /* Nav to new pump record */ }
                    )

                    // Action 2: Thêm nông dân
                    QuickActionOutlineCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.PersonAddAlt1,
                        iconTint = AgriGreenPrimary,
                        text = "Thêm\nnông dân",
                        onClick = onNavigateToFarmers
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Action 3: Thu tiền nợ
                    QuickActionOutlineCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.AutoMirrored.Filled.ReceiptLong,
                        iconTint = AgriGreenPrimary,
                        text = "Thu tiền\nnợ",
                        onClick = { /* Nav to collect debt */ }
                    )

                    // Action 4: Báo cáo mùa vụ / Thiết lập đơn giá
                    QuickActionOutlineCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.BarChart,
                        iconTint = AgriGreenPrimary,
                        text = "Báo cáo\nmùa vụ",
                        onClick = onNavigateToPricing
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 5. Recent Transactions Section ("Giao dịch gần đây")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Giao dịch gần đây",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Text(
                        text = "Xem tất cả",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriGreenPrimary,
                        modifier = Modifier.clickable { /* View all transactions */ }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Recent Transactions Card List
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val transactions = uiState.dashboardSummary?.recentTransactions.orEmpty()

                        if (transactions.isEmpty()) {
                            // Demo fallback items if empty
                            TransactionItemRow(
                                item = RecentTransaction(
                                    id = "demo-1",
                                    type = "PUMP",
                                    farmerName = "Nguyễn Văn An",
                                    details = "Bơm 2.5 công • 08:30 sáng",
                                    amount = java.math.BigDecimal("500000"),
                                    formattedAmount = "+ 500.000đ",
                                    statusBadge = "Ghi nợ",
                                    createdAt = null
                                )
                            )
                            HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.5f), thickness = 1.dp)
                            TransactionItemRow(
                                item = RecentTransaction(
                                    id = "demo-2",
                                    type = "PAYMENT",
                                    farmerName = "Lê Thị Bé",
                                    details = "Thu nợ cũ • Hôm qua",
                                    amount = java.math.BigDecimal("-1200000"),
                                    formattedAmount = "- 1.200.000đ",
                                    statusBadge = "Đã thu",
                                    createdAt = null
                                )
                            )
                        } else {
                            transactions.forEachIndexed { index, tx ->
                                TransactionItemRow(item = tx)
                                if (index < transactions.size - 1) {
                                    HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.5f), thickness = 1.dp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    isOffline: Boolean,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Leaf / Water Drop Circular Icon Container
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(AgriMintContainer)
                    .border(1.dp, AgriMintBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = AgriGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Bơm Nước",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AgriGreenPrimary,
                letterSpacing = (-0.3).sp
            )

            if (isOffline) {
                Spacer(modifier = Modifier.width(8.dp))
                OfflineStatusBadge()
            }
        }

        IconButton(onClick = onNotificationClick) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Thông báo",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun MetricCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    value: String,
    valueSuffix: String? = null,
    valueColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF475569)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = valueColor,
                    letterSpacing = (-0.5).sp
                )
                if (valueSuffix != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = valueSuffix,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionPrimaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(104.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AgriGreenPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun QuickActionOutlineCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(104.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun TransactionItemRow(item: RecentTransaction) {
    val isPump = item.type == "PUMP"
    val avatarBg = if (isPump) Color(0xFFF3EDE4) else Color(0xFFE8F5E9)
    val avatarTint = if (isPump) Color(0xFF8D6E63) else AgriGreenPrimary
    val badgeBg = if (isPump) Color(0xFFFEE2E2) else Color(0xFFDCFCE7)
    val badgeText = if (isPump) Color(0xFFDC2626) else AgriGreenPrimary
    val amountColor = if (isPump) Color(0xFFDC2626) else Color(0xFF0F766E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icon Avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(avatarBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPump) Icons.Default.WaterDrop else Icons.Default.Payments,
                    contentDescription = null,
                    tint = avatarTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = item.farmerName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.details,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.formattedAmount,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(badgeBg)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = item.statusBadge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeText
                )
            }
        }
    }
}
