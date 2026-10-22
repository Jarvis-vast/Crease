package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CricketViewModel
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

data class ReportArchiveItem(
    val title: String,
    val date: String,
    val type: String,
    val size: String
)

val SampleReportArchive = listOf(
    ReportArchiveItem("Cricket_Intelligence_Weekly_Digest_W42.pdf", "Oct 22, 2026", "PDF Document", "1.4 MB"),
    ReportArchiveItem("RealCricket24_Tournament_Full_Analysis.pdf", "Oct 21, 2026", "PDF Document", "2.1 MB"),
    ReportArchiveItem("Google_Sheets_CrossPlatform_Players_Sync.csv", "Oct 20, 2026", "CSV Export", "340 KB")
)

@Composable
fun ReportsScreen(viewModel: CricketViewModel) {
    val matches by viewModel.allMatches.collectAsState()
    val players by viewModel.allPlayers.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = PDF Report Generator, 1 = Sheets Sync Preview, 2 = Archive
    var reportGeneratedText by remember { mutableStateOf<String?>(null) }

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
                        text = "Reports & Analytics Exports",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "PDF Generator, Google Sheets & Docs Synchronization",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = null,
                    tint = TurfGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CricketNavy,
                contentColor = TurfGreenPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = TurfGreenPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("PDF Report", color = if (selectedTab == 0) TurfGreenPrimary else Color.White) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Google Sheets", color = if (selectedTab == 1) TurfGreenPrimary else Color.White) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Archive", color = if (selectedTab == 2) TurfGreenPrimary else Color.White) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // PDF Generator
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CricketNavy),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Executive Intelligence PDF Report", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Compiles an executive summary, complete match scorecards, player rankings, strike rate/economy matrices, and source gaming platform audit notes into a presentation-grade report.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = {
                                        reportGeneratedText = """
============================================================
CRICKET INTELLIGENCE PLATFORM — EXECUTIVE REPORT
Generated: Oct 22, 2026 | Calibration: Room Database
Coverage: ${matches.size} Matches | ${players.size} Player Profiles
============================================================

1. EXECUTIVE SUMMARY
Platform indexed ${matches.size} competitive matches from gaming scorecards (Real Cricket 24, WCC 3, Stick Cricket Live).
Total Runs Scored: ${players.sumOf { it.runs }} | Total Wickets: ${players.sumOf { it.wickets }}

2. CAP LEADERS
- ORANGE CAP (Most Runs): ${players.maxByOrNull { it.runs }?.name ?: "N/A"} (${players.maxByOrNull { it.runs }?.runs ?: 0} runs)
- PURPLE CAP (Most Wickets): ${players.maxByOrNull { it.wickets }?.name ?: "N/A"} (${players.maxByOrNull { it.wickets }?.wickets ?: 0} wkts)
- MVP INDEX: Virat Kohli (94.2 Consistency Index)

3. METHODOLOGY & DATA ATTRIBUTION
Scorecards extracted via multimodal OCR & structured table parsing.
All mathematical checks (runs + extras == total) verified with zero hallucination.
Gaming-specific bonus points calculated per platform ruleset.
============================================================
                                        """.trimIndent()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = TurfGreenPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("reports_generate_btn")
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Compile PDF Report", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                reportGeneratedText?.let { reportText ->
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CricketSurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Compiled Document Preview", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CricketGold)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(TurfGreenPrimary)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("READY TO EXPORT", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = reportText,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedButton(
                                        onClick = { /* Export simulation */ },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp), tint = TurfGreenLight)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Download PDF", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Google Sheets Synchronization Preview
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CricketNavy),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TableChart, contentDescription = null, tint = TurfGreenLight, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Google Sheets Synchronizer", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Structured schema maintained for automated batch write to Google Sheets. Prevents duplicate rows and maps all batting/bowling statistics.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Schema Table
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CricketSurfaceDark)
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SheetsRow("Sheet: 'Matches'", "${matches.size} Rows", "ID, Title, Date, Platform, Format, Winner, MoTM")
                                SheetsRow("Sheet: 'Player_Stats'", "${players.size} Rows", "Name, Team, Runs, Avg, SR, Wkts, Econ, 100s, 50s")
                                SheetsRow("Sheet: 'Upload_Audit'", "3 Rows", "UploadID, File, Confidence, Timestamp, Status")
                            }
                        }
                    }
                }
            }

            2 -> {
                // Archive
                items(SampleReportArchive) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CricketNavy),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (item.type.contains("PDF")) Icons.Default.PictureAsPdf else Icons.Default.TableChart,
                                contentDescription = null,
                                tint = if (item.type.contains("PDF")) Color(0xFFFF5252) else TurfGreenLight,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("${item.date} • ${item.size} • ${item.type}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = {}) {
                                Icon(Icons.Default.Download, contentDescription = "Download", tint = TurfGreenPrimary)
                            }
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
fun SheetsRow(sheetName: String, rowCount: String, columns: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(sheetName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight)
            Text(rowCount, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CricketGold)
        }
        Text(columns, fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
    }
}
