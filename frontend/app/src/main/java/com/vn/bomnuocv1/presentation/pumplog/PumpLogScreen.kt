package com.vn.bomnuocv1.presentation.pumplog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.bomnuocv1.presentation.common.AgriButton
import com.vn.bomnuocv1.presentation.common.AppBottomNavigationBar
import com.vn.bomnuocv1.presentation.home.HomeBottomTab
import com.vn.bomnuocv1.ui.theme.AgriBackground
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriGreenDark
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimaryContainer
import com.vn.bomnuocv1.ui.theme.AgriMintBorder
import com.vn.bomnuocv1.ui.theme.AgriMintContainer

private data class DemoPumpRecord(
    val id: String,
    val farmerName: String,
    val time: String,
    val quantity: String,
    val unitPrice: String,
    val totalAmount: String,
    val isPaid: Boolean
)

@Composable
fun PumpLogScreen(
    onTabSelected: (HomeBottomTab) -> Unit,
    onNavigateToNewPump: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val demoRecords = remember {
        listOf(
            DemoPumpRecord("1", "Nguyễn Văn Bảy", "Hôm nay, 08:30", "3.5 công lớn (3m)", "115.000 đ", "402.500 đ", false),
            DemoPumpRecord("2", "Trần Văn Sáu", "Hôm nay, 06:15", "2.0 công nhỏ (1.000m²)", "90.000 đ", "180.000 đ", true),
            DemoPumpRecord("3", "Lê Văn Tám", "Hôm qua, 16:45", "1.5 giờ", "60.000 đ", "90.000 đ", false),
            DemoPumpRecord("4", "Phạm Thị Năm", "Hôm qua, 14:20", "4.0 công lớn (3m)", "115.000 đ", "460.000 đ", true),
            DemoPumpRecord("5", "Võ Văn Mười", "01/09, 09:00", "2.5 công nhỏ (1.000m²)", "90.000 đ", "225.000 đ", false)
        )
    }

    Scaffold(
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
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
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
                            text = "Hôm nay: 12 lượt",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenDark
                        )
                    }
                }
            }

            HorizontalDivider(color = AgriCardBorder.copy(alpha = 0.6f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Search and Filter Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Tìm theo tên nông dân...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AgriGreenPrimary,
                            unfocusedBorderColor = AgriCardBorder,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, AgriCardBorder)
                    ) {
                        IconButton(onClick = { /* Filter click */ }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Lọc",
                                tint = Color(0xFF475569)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Summary Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Tổng diện tích", fontSize = 11.sp, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "28 công", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(AgriCardBorder))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Tổng tiền tạm tính", fontSize = 11.sp, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "2.850.000 đ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Records Header
                Text(
                    text = "Lịch sử bơm gần đây",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Records List
                demoRecords.forEach { record ->
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
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (record.isPaid) AgriMintContainer else Color(0xFFF1F5F9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = null,
                                        tint = if (record.isPaid) AgriGreenDark else Color(0xFF0284C7),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = record.farmerName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${record.quantity} • ${record.time}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = record.totalAmount,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgriGreenPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (record.isPaid) AgriMintContainer else Color(0xFFFEF3C7)
                                ) {
                                    Text(
                                        text = if (record.isPaid) "Đã thu" else "Chưa thu",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (record.isPaid) AgriGreenDark else Color(0xFFB45309),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
