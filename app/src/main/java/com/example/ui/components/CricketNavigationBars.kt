package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenTab
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenPrimary

data class NavTabItem(
    val tab: ScreenTab,
    val title: String,
    val icon: ImageVector
)

val AllNavTabs = listOf(
    NavTabItem(ScreenTab.OVERVIEW, "Overview", Icons.Default.Dashboard),
    NavTabItem(ScreenTab.MATCH_CENTER, "Match Center", Icons.Default.SportsCricket),
    NavTabItem(ScreenTab.SCORECARDS, "Scorecards", Icons.Default.Scoreboard),
    NavTabItem(ScreenTab.PLAYERS, "Players", Icons.Default.Person),
    NavTabItem(ScreenTab.TEAMS_TOURNAMENTS, "Teams & Leagues", Icons.Default.Groups),
    NavTabItem(ScreenTab.ANALYTICS_LAB, "Analytics Lab", Icons.Default.Analytics),
    NavTabItem(ScreenTab.UPLOAD_REVIEW, "Upload & Review", Icons.Default.CloudUpload),
    NavTabItem(ScreenTab.REPORTS, "Reports & Docs", Icons.Default.Assessment),
    NavTabItem(ScreenTab.INTEGRATIONS, "Integrations", Icons.Default.Hub),
    NavTabItem(ScreenTab.SETTINGS_AUDIT, "Settings & Audit", Icons.Default.Settings)
)

@Composable
fun TopTabScrollRow(
    selectedTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        color = CricketNavyDark,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AllNavTabs.forEach { item ->
                val isSelected = selectedTab == item.tab
                FilterChip(
                    selected = isSelected,
                    onClick = { onTabSelected(item.tab) },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TurfGreenPrimary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = CricketSurfaceDark,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        iconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = null,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("tab_chip_${item.tab.name.lowercase()}")
                )
            }
        }
    }
}

@Composable
fun CricketBottomNavigation(
    selectedTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit
) {
    val bottomPrimaryTabs = listOf(
        NavTabItem(ScreenTab.OVERVIEW, "Overview", Icons.Default.Dashboard),
        NavTabItem(ScreenTab.MATCH_CENTER, "Matches", Icons.Default.SportsCricket),
        NavTabItem(ScreenTab.UPLOAD_REVIEW, "Upload", Icons.Default.CloudUpload),
        NavTabItem(ScreenTab.PLAYERS, "Players", Icons.Default.Person),
        NavTabItem(ScreenTab.ANALYTICS_LAB, "Analytics", Icons.Default.Analytics)
    )

    NavigationBar(
        containerColor = CricketNavyDark,
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        bottomPrimaryTabs.forEach { item ->
            val isSelected = selectedTab == item.tab
            val isUpload = item.tab == ScreenTab.UPLOAD_REVIEW

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    if (isUpload) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) CricketGold else TurfGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = CricketNavyDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TurfGreenPrimary,
                    selectedTextColor = TurfGreenPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = CricketSurfaceDark
                ),
                modifier = Modifier.testTag("nav_item_${item.tab.name.lowercase()}")
            )
        }
    }
}
