package com.vn.bomnuocv1.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vn.bomnuocv1.presentation.home.HomeBottomTab
import com.vn.bomnuocv1.ui.theme.AgriCardBorder
import com.vn.bomnuocv1.ui.theme.AgriGreenPrimary

private data class TabItemConfig(
    val tab: HomeBottomTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomTabConfigs = listOf(
    TabItemConfig(
        tab = HomeBottomTab.HOME,
        label = "Trang chủ",
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    TabItemConfig(
        tab = HomeBottomTab.PUMP_LOG,
        label = "Lượt bơm",
        selectedIcon = Icons.Default.WaterDrop,
        unselectedIcon = Icons.Outlined.WaterDrop
    ),
    TabItemConfig(
        tab = HomeBottomTab.DEBT_LEDGER,
        label = "Sổ nợ",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.AutoStories
    ),
    TabItemConfig(
        tab = HomeBottomTab.SETTINGS,
        label = "Cài đặt",
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

/**
 * Floating Pill Bottom Navigation Bar with 4 rounded corners.
 * Active tabs are highlighted in vibrant green capsule with white text & icon.
 */
@Composable
fun AppBottomNavigationBar(
    selectedTab: HomeBottomTab,
    onTabSelected: (HomeBottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 10.dp,
        border = BorderStroke(1.dp, AgriCardBorder.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomTabConfigs.forEach { config ->
                val isSelected = selectedTab == config.tab

                if (isSelected) {
                    // Active Pill Capsule
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(AgriGreenPrimary)
                            .clickable { onTabSelected(config.tab) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = config.selectedIcon,
                                contentDescription = config.label,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = config.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                } else {
                    // Inactive Tab Item
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTabSelected(config.tab) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = config.unselectedIcon,
                            contentDescription = config.label,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = config.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
