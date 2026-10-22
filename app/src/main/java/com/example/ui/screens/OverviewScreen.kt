package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MatchEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.UploadRecordEntity
import com.example.ui.CricketViewModel
import com.example.ui.ScreenTab
import com.example.ui.theme.BoundaryRed
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketNavyLight
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

@Composable
fun OverviewScreen(
    viewModel: CricketViewModel,
    onNavigateTab: (ScreenTab) -> Unit,
    onSelectMatch: (String) -> Unit,
    onSelectPlayer: (String) -> Unit
) {
    val matches by viewModel.allMatches.collectAsState()
    val players by viewModel.allPlayers.collectAsState()
    val uploads by viewModel.allUploads.collectAsState()

    val totalRuns = players.sumOf { it.runs }
    val totalWickets = players.sumOf { it.wickets }
    val topBatter = players.maxByOrNull { it.runs }
    val topBowler = players.maxByOrNull { it.wickets }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CricketNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Banner
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(CricketNavy, TurfGreenDark)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(TurfGreenLight)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GAMING CRICKET INTELLIGENCE HUB",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TurfGreenLight,
                                letterSpacing = 1.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CricketNavyDark.copy(alpha = 0.6f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "STANDALONE READY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CricketGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Scorecard OCR & Multi-Platform Analytics",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Text(
                        text = "Turn Real Cricket 24, WCC 3 & Stick Cricket scorecards into Cricbuzz-grade analytics, player profiles & automated briefs.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onNavigateTab(ScreenTab.UPLOAD_REVIEW) },
                            colors = ButtonDefaults.buttonColors(containerColor = TurfGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("overview_hero_upload_btn")
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload Scorecard", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { viewModel.triggerMorningBriefing() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("overview_hero_briefing_btn")
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = CricketGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send Briefing")
                        }
                    }
                }
            }
        }

        // Key Performance Indicators Grid
        item {
            Text(
                text = "Key Performance Indicators",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiCard(
                    title = "Matches Indexed",
                    value = "${matches.size}",
                    subtext = "Across 3 Games",
                    accentColor = TurfGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Total Runs",
                    value = "$totalRuns",
                    subtext = "Avg 8.85 RPO",
                    accentColor = CricketGold,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Total Wickets",
                    value = "$totalWickets",
                    subtext = "Econ 7.42",
                    accentColor = BoundaryRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Top Performers Spotlight (Orange Cap & Purple Cap)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Caps & Top Performers",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "See All Players",
                    fontSize = 12.sp,
                    color = TurfGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateTab(ScreenTab.PLAYERS) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Orange Cap Card
                topBatter?.let { batter ->
                    CapCard(
                        capName = "ORANGE CAP (Runs)",
                        playerName = batter.name,
                        team = batter.team,
                        primaryStat = "${batter.runs} Runs",
                        secondaryStat = "SR ${batter.strikeRate} | Avg ${batter.battingAverage}",
                        accentColor = CricketGold,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectPlayer(batter.id) }
                    )
                }

                // Purple Cap Card
                topBowler?.let { bowler ->
                    CapCard(
                        capName = "PURPLE CAP (Wickets)",
                        playerName = bowler.name,
                        team = bowler.team,
                        primaryStat = "${bowler.wickets} Wickets",
                        secondaryStat = "Econ ${bowler.economy} | BBI ${bowler.bestBowling}",
                        accentColor = Color(0xFFB388FF),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectPlayer(bowler.id) }
                    )
                }
            }
        }

        // Recent Matches Carousel
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Matches",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Match Center",
                    fontSize = 12.sp,
                    color = TurfGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateTab(ScreenTab.MATCH_CENTER) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (matches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No matches uploaded yet. Tap Upload Scorecard to start!", color = Color.Gray)
                }
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(matches) { match ->
                        MatchCardItem(match = match, onClick = { onSelectMatch(match.id) })
                    }
                }
            }
        }

        // Recent Uploads Activity Log
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Upload & Extraction Pipeline",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Upload Manager",
                    fontSize = 12.sp,
                    color = TurfGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateTab(ScreenTab.UPLOAD_REVIEW) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            uploads.take(3).forEach { upload ->
                UploadLogCard(upload)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtext: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CricketNavy),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtext, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun CapCard(
    capName: String,
    playerName: String,
    team: String,
    primaryStat: String,
    secondaryStat: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CricketNavy),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = capName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = playerName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = team,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = primaryStat,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
            Text(
                text = secondaryStat,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun MatchCardItem(
    match: MatchEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CricketNavy),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() }
            .testTag("overview_match_${match.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CricketSurfaceDark)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = match.sourcePlatform,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TurfGreenLight
                    )
                }

                Text(
                    text = match.format,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CricketGold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = match.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = match.resultText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TurfGreenPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MoTM: ${match.manOfTheMatch}",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = TurfGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun UploadLogCard(upload: UploadRecordEntity) {
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
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (upload.status == "CONFIRMED") TurfGreenPrimary.copy(alpha = 0.2f) else CricketGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (upload.status == "CONFIRMED") TurfGreenPrimary else CricketGold,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = upload.fileName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${upload.sourcePlatform} • Confidence: ${(upload.confidence * 100).toInt()}%",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(CricketNavyDark)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = upload.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (upload.status == "CONFIRMED") TurfGreenLight else CricketGold
                )
            }
        }
    }
}
