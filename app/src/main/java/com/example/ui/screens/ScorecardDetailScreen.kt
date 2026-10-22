package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.BattingRecordEntity
import com.example.data.model.BowlingRecordEntity
import com.example.data.model.InningsEntity
import com.example.data.model.MatchEntity
import com.example.ui.CricketViewModel
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

@Composable
fun ScorecardDetailScreen(
    viewModel: CricketViewModel,
    matchId: String?,
    onBack: () -> Unit
) {
    val matches by viewModel.allMatches.collectAsState()
    val match = matches.find { it.id == matchId } ?: matches.firstOrNull()

    var selectedInningsTab by remember { mutableIntStateOf(0) } // 0 = 1st innings, 1 = 2nd innings

    if (match == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CricketNavyDark),
            contentAlignment = Alignment.Center
        ) {
            Text("No match selected. Please choose a match from Match Center.", color = Color.White)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CricketNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Back & Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("scorecard_back_btn")) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = match.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "${match.tournament} • ${match.sourcePlatform}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CricketSurfaceDark)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = match.format,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CricketGold
                    )
                }
            }
        }

        // Match Result Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = match.resultText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TurfGreenLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${match.tossResult} • Venue: ${match.venue}",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = CricketGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Player of the Match: ${match.manOfTheMatch}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CricketGold
                        )
                    }
                }
            }
        }

        // Innings Tab Switcher
        item {
            TabRow(
                selectedTabIndex = selectedInningsTab,
                containerColor = CricketNavy,
                contentColor = TurfGreenPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedInningsTab]),
                        color = TurfGreenPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedInningsTab == 0,
                    onClick = { selectedInningsTab = 0 },
                    text = {
                        Text(
                            text = "1st Innings: ${match.team1}",
                            fontWeight = if (selectedInningsTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedInningsTab == 0) TurfGreenPrimary else Color.White
                        )
                    }
                )
                Tab(
                    selected = selectedInningsTab == 1,
                    onClick = { selectedInningsTab = 1 },
                    text = {
                        Text(
                            text = "2nd Innings: ${match.team2}",
                            fontWeight = if (selectedInningsTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedInningsTab == 1) TurfGreenPrimary else Color.White
                        )
                    }
                )
            }
        }

        // Batting Card Table (ESPNcricinfo / Cricbuzz style)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Batting Scorecard",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CricketSurfaceDark)
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Batter", modifier = Modifier.weight(1.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CricketGold)
                        Text("R", modifier = Modifier.weight(0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("B", modifier = Modifier.weight(0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("4s", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("6s", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("SR", modifier = Modifier.weight(0.9f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight, textAlign = TextAlign.End)
                    }

                    HorizontalDivider(color = CricketNavyDark)

                    // Batter Rows (Render realistic innings depending on tab)
                    val sampleBatters = if (selectedInningsTab == 0) {
                        listOf(
                            Triple("Rohit Sharma", "c Maxwell b Starc", listOf(47, 24, 6, 2, 195.83f)),
                            Triple("Virat Kohli", "not out", listOf(76, 52, 6, 2, 146.15f)),
                            Triple("Rishabh Pant", "c Marsh b Cummins", listOf(21, 14, 2, 1, 150.00f)),
                            Triple("Suryakumar Yadav", "c Smith b Zampa", listOf(34, 18, 3, 2, 188.89f)),
                            Triple("Hardik Pandya", "not out", listOf(24, 12, 1, 2, 200.00f))
                        )
                    } else {
                        listOf(
                            Triple("Travis Head", "b Bumrah", listOf(38, 22, 5, 1, 172.72f)),
                            Triple("David Warner", "c Pant b Arshdeep", listOf(18, 12, 3, 0, 150.00f)),
                            Triple("Mitchell Marsh", "c Kohli b Kuldeep", listOf(28, 19, 2, 1, 147.36f)),
                            Triple("Glenn Maxwell", "b Bumrah", listOf(44, 26, 4, 2, 169.23f)),
                            Triple("Marcus Stoinis", "c Jadeja b Pandya", listOf(22, 14, 2, 1, 157.14f)),
                            Triple("Pat Cummins", "not out", listOf(16, 9, 1, 1, 177.77f))
                        )
                    }

                    sampleBatters.forEach { (name, dismissal, stats) ->
                        BattingRow(
                            name = name,
                            dismissal = dismissal,
                            runs = stats[0].toInt(),
                            balls = stats[1].toInt(),
                            fours = stats[2].toInt(),
                            sixes = stats[3].toInt(),
                            sr = stats[4].toFloat()
                        )
                        HorizontalDivider(color = CricketSurfaceDark.copy(alpha = 0.5f))
                    }

                    // Total & Extras summary
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CricketSurfaceDark)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (selectedInningsTab == 0) "TOTAL: 202/3 (20.0 Ov, CRR: 10.10)" else "TOTAL: 184/5 (20.0 Ov, CRR: 9.20)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Extras: 6 (b 1, lb 2, w 3)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Bowling Table
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Bowling Figures",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CricketSurfaceDark)
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bowler", modifier = Modifier.weight(1.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CricketGold)
                        Text("O", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("M", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("R", modifier = Modifier.weight(0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                        Text("W", modifier = Modifier.weight(0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight, textAlign = TextAlign.End)
                        Text("Econ", modifier = Modifier.weight(0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                    }

                    HorizontalDivider(color = CricketNavyDark)

                    val sampleBowlers = if (selectedInningsTab == 0) {
                        listOf(
                            Triple("Mitchell Starc", listOf(4.0f, 0f, 42f, 1f, 10.50f), "8 dots"),
                            Triple("Pat Cummins", listOf(4.0f, 0f, 36f, 1f, 9.00f), "10 dots"),
                            Triple("Josh Hazlewood", listOf(4.0f, 0f, 38f, 0f, 9.50f), "9 dots"),
                            Triple("Adam Zampa", listOf(4.0f, 0f, 44f, 1f, 11.00f), "7 dots")
                        )
                    } else {
                        listOf(
                            Triple("Jasprit Bumrah", listOf(4.0f, 1f, 24f, 2f, 6.00f), "16 dots"),
                            Triple("Arshdeep Singh", listOf(4.0f, 0f, 38f, 1f, 9.50f), "9 dots"),
                            Triple("Kuldeep Yadav", listOf(4.0f, 0f, 32f, 1f, 8.00f), "11 dots"),
                            Triple("Hardik Pandya", listOf(4.0f, 0f, 44f, 1f, 11.00f), "7 dots")
                        )
                    }

                    sampleBowlers.forEach { (name, stats, _) ->
                        BowlingRow(
                            name = name,
                            overs = stats[0].toFloat(),
                            maidens = stats[1].toInt(),
                            runs = stats[2].toInt(),
                            wickets = stats[3].toInt(),
                            econ = stats[4].toFloat()
                        )
                        HorizontalDivider(color = CricketSurfaceDark.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Fall of Wickets Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketSurfaceDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Fall of Wickets",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CricketGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (selectedInningsTab == 0)
                            "1-47 (Rohit Sharma, 4.3 ov), 2-98 (Rishabh Pant, 10.2 ov), 3-156 (Suryakumar Yadav, 15.5 ov)"
                        else
                            "1-28 (David Warner, 3.1 ov), 2-64 (Travis Head, 7.2 ov), 3-112 (Mitchell Marsh, 12.4 ov), 4-152 (Glenn Maxwell, 16.3 ov)",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BattingRow(
    name: String,
    dismissal: String,
    runs: Int,
    balls: Int,
    fours: Int,
    sixes: Int,
    sr: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.8f)) {
            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = dismissal,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text("$runs", modifier = Modifier.weight(0.6f), fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (runs >= 50) CricketGold else Color.White, textAlign = TextAlign.End)
        Text("$balls", modifier = Modifier.weight(0.6f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.End)
        Text("$fours", modifier = Modifier.weight(0.5f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.End)
        Text("$sixes", modifier = Modifier.weight(0.5f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.End)
        Text(String.format("%.1f", sr), modifier = Modifier.weight(0.9f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight, textAlign = TextAlign.End)
    }
}

@Composable
fun BowlingRow(
    name: String,
    overs: Float,
    maidens: Int,
    runs: Int,
    wickets: Int,
    econ: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, modifier = Modifier.weight(1.8f), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(String.format("%.1f", overs), modifier = Modifier.weight(0.5f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.End)
        Text("$maidens", modifier = Modifier.weight(0.5f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.End)
        Text("$runs", modifier = Modifier.weight(0.6f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), textAlign = TextAlign.End)
        Text("$wickets", modifier = Modifier.weight(0.6f), fontSize = 13.sp, fontWeight = FontWeight.Black, color = if (wickets >= 2) TurfGreenLight else Color.White, textAlign = TextAlign.End)
        Text(String.format("%.2f", econ), modifier = Modifier.weight(0.8f), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (econ < 7.0f) TurfGreenLight else Color.White, textAlign = TextAlign.End)
    }
}
