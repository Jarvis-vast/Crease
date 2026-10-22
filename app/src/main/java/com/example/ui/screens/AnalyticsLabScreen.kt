package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PlayerProfileEntity
import com.example.ui.CricketViewModel
import com.example.ui.theme.BoundaryRed
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsLabScreen(viewModel: CricketViewModel) {
    val players by viewModel.allPlayers.collectAsState()
    val compare1Id by viewModel.comparePlayer1Id.collectAsState()
    val compare2Id by viewModel.comparePlayer2Id.collectAsState()

    var selectedSection by remember { mutableIntStateOf(0) } // 0 = Head-to-Head, 1 = Leaderboards, 2 = Platform Stats

    val p1 = players.find { it.id == compare1Id } ?: players.firstOrNull()
    val p2 = players.find { it.id == compare2Id } ?: players.getOrNull(1) ?: p1

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CricketNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Analytics Lab",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Advanced Player Comparisons & Statistical Indices",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = TurfGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Section Tabs
        item {
            TabRow(
                selectedTabIndex = selectedSection,
                containerColor = CricketNavy,
                contentColor = TurfGreenPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                        color = TurfGreenPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedSection == 0,
                    onClick = { selectedSection = 0 },
                    text = { Text("H2H Compare", color = if (selectedSection == 0) TurfGreenPrimary else Color.White) }
                )
                Tab(
                    selected = selectedSection == 1,
                    onClick = { selectedSection = 1 },
                    text = { Text("Leaderboards", color = if (selectedSection == 1) TurfGreenPrimary else Color.White) }
                )
                Tab(
                    selected = selectedSection == 2,
                    onClick = { selectedSection = 2 },
                    text = { Text("Platform Matrix", color = if (selectedSection == 2) TurfGreenPrimary else Color.White) }
                )
            }
        }

        when (selectedSection) {
            0 -> {
                // Head-to-Head Comparison
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CricketNavy),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Select Players to Compare",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(Icons.Default.CompareArrows, contentDescription = null, tint = CricketGold)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Player Pickers (Dropdowns or Chips)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                PlayerDropdownSelector(
                                    label = "Player 1",
                                    selectedPlayer = p1,
                                    allPlayers = players,
                                    onSelect = { viewModel.setComparePlayer1(it.id) },
                                    modifier = Modifier.weight(1f)
                                )

                                PlayerDropdownSelector(
                                    label = "Player 2",
                                    selectedPlayer = p2,
                                    allPlayers = players,
                                    onSelect = { viewModel.setComparePlayer2(it.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                if (p1 != null && p2 != null) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CricketNavy),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(p1.name, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TurfGreenLight)
                                        Text(p1.team, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("VS", fontWeight = FontWeight.Black, fontSize = 16.sp, color = CricketGold)
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(p2.name, fontWeight = FontWeight.Black, fontSize = 16.sp, color = InfoBlue)
                                        Text(p2.team, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Comparison Metric Bars
                                ComparisonMetricBar(label = "Total Runs", val1 = p1.runs.toFloat(), val2 = p2.runs.toFloat(), display1 = "${p1.runs}", display2 = "${p2.runs}")
                                ComparisonMetricBar(label = "Batting Average", val1 = p1.battingAverage, val2 = p2.battingAverage, display1 = "${p1.battingAverage}", display2 = "${p2.battingAverage}")
                                ComparisonMetricBar(label = "Strike Rate", val1 = p1.strikeRate, val2 = p2.strikeRate, display1 = "${p1.strikeRate}", display2 = "${p2.strikeRate}")
                                ComparisonMetricBar(label = "Wickets", val1 = p1.wickets.toFloat(), val2 = p2.wickets.toFloat(), display1 = "${p1.wickets}", display2 = "${p2.wickets}")
                                ComparisonMetricBar(label = "Consistency Index", val1 = p1.consistencyIndex, val2 = p2.consistencyIndex, display1 = "${p1.consistencyIndex}%", display2 = "${p2.consistencyIndex}%")
                                ComparisonMetricBar(label = "Boundaries (4s + 6s)", val1 = (p1.fours + p1.sixes).toFloat(), val2 = (p2.fours + p2.sixes).toFloat(), display1 = "${p1.fours + p1.sixes}", display2 = "${p2.fours + p2.sixes}")
                            }
                        }
                    }
                }
            }

            1 -> {
                // Leaderboards
                item {
                    Text("Top Run Scorers (Orange Cap Race)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CricketGold)
                }

                items(players.sortedByDescending { it.runs }.take(5)) { player ->
                    LeaderboardRow(
                        rank = players.sortedByDescending { it.runs }.indexOf(player) + 1,
                        name = player.name,
                        team = player.team,
                        stat = "${player.runs} Runs",
                        subtext = "Avg ${player.battingAverage} • SR ${player.strikeRate}",
                        highlightColor = CricketGold
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Top Wicket Takers (Purple Cap Race)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB388FF))
                }

                items(players.sortedByDescending { it.wickets }.take(5)) { player ->
                    LeaderboardRow(
                        rank = players.sortedByDescending { it.wickets }.indexOf(player) + 1,
                        name = player.name,
                        team = player.team,
                        stat = "${player.wickets} Wickets",
                        subtext = "Econ ${player.economy} • BBI ${player.bestBowling}",
                        highlightColor = Color(0xFFB388FF)
                    )
                }
            }

            2 -> {
                // Platform Matrix
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CricketNavy),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Platform Scorecard Distribution",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Traceability of indexed datasets across gaming titles",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            PlatformStatRow("Real Cricket 24", 55, TurfGreenPrimary)
                            PlatformStatRow("World Cricket Championship 3", 28, CricketGold)
                            PlatformStatRow("Stick Cricket Live", 12, InfoBlue)
                            PlatformStatRow("Custom Leagues / OCR Uploads", 5, BoundaryRed)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ComparisonMetricBar(
    label: String,
    val1: Float,
    val2: Float,
    display1: String,
    display2: String
) {
    val maxVal = maxOf(val1, val2).coerceAtLeast(1.0f)
    val ratio1 = (val1 / maxVal).coerceIn(0.1f, 1.0f)
    val ratio2 = (val2 / maxVal).coerceIn(0.1f, 1.0f)

    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(display1, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight)
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(display2, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InfoBlue)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(CricketSurfaceDark)
        ) {
            Box(
                modifier = Modifier
                    .weight(ratio1)
                    .fillMaxHeight()
                    .background(TurfGreenLight)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .weight(ratio2)
                    .fillMaxHeight()
                    .background(InfoBlue)
            )
        }
    }
}

@Composable
fun LeaderboardRow(
    rank: Int,
    name: String,
    team: String,
    stat: String,
    subtext: String,
    highlightColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CricketSurfaceDark),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (rank == 1) CricketGold else Color.White,
                modifier = Modifier.width(28.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(team, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(stat, fontSize = 14.sp, fontWeight = FontWeight.Black, color = highlightColor)
                Text(subtext, fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun PlatformStatRow(
    name: String,
    percentage: Int,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 12.sp, color = Color.White)
            Text("$percentage%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CricketNavyDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage / 100f)
                    .background(color)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDropdownSelector(
    label: String,
    selectedPlayer: PlayerProfileEntity?,
    allPlayers: List<PlayerProfileEntity>,
    onSelect: (PlayerProfileEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedPlayer?.name ?: "Select",
            onValueChange = {},
            readOnly = true,
            label = { Text(label, fontSize = 10.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CricketSurfaceDark,
                unfocusedContainerColor = CricketSurfaceDark,
                focusedBorderColor = TurfGreenPrimary,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(CricketNavy)
        ) {
            allPlayers.forEach { player ->
                DropdownMenuItem(
                    text = { Text(player.name, color = Color.White) },
                    onClick = {
                        onSelect(player)
                        expanded = false
                    }
                )
            }
        }
    }
}
