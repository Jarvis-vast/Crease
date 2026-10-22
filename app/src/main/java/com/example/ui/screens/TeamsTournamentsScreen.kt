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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CricketViewModel
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

data class TeamStanding(
    val rank: Int,
    val team: String,
    val played: Int,
    val won: Int,
    val lost: Int,
    val nrr: String,
    val points: Int
)

val SampleStandings = listOf(
    TeamStanding(1, "India (RC24)", 6, 5, 1, "+1.420", 10),
    TeamStanding(2, "Australia (RC24)", 6, 4, 2, "+0.850", 8),
    TeamStanding(3, "Royal Challengers (WCC3)", 5, 3, 2, "+0.310", 6),
    TeamStanding(4, "Chennai Kings (Stick)", 5, 3, 2, "+0.150", 6),
    TeamStanding(5, "Mumbai Titans (WCC3)", 5, 2, 3, "-0.280", 4),
    TeamStanding(6, "Kolkata Knights (Stick)", 5, 1, 4, "-0.940", 2)
)

data class TeamRoster(
    val teamName: String,
    val league: String,
    val captain: String,
    val starPlayers: List<String>
)

val SampleRosters = listOf(
    TeamRoster("India / Royal Challengers", "Real Cricket 24 & WCC 3", "Rohit Sharma", listOf("Virat Kohli", "Jasprit Bumrah", "Suryakumar Yadav", "Hardik Pandya")),
    TeamRoster("Australia / Sunrisers", "Real Cricket 24 & Stick", "Pat Cummins", listOf("Travis Head", "Mitchell Starc", "Glenn Maxwell", "Josh Hazlewood")),
    TeamRoster("Mumbai Titans", "WCC 3 Blitz League", "Hardik Pandya", listOf("Rohit Sharma", "Jasprit Bumrah", "Ishan Kishan", "Trent Boult")),
    TeamRoster("Chennai Kings", "Stick Cricket Premier", "Ruturaj Gaikwad", listOf("MS Dhoni", "Ravindra Jadeja", "Matheesha Pathirana", "Shivam Dube"))
)

@Composable
fun TeamsTournamentsScreen(viewModel: CricketViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Standings, 1 = Rosters

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
                        text = "Teams & Tournaments",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Multi-Platform Leagues Standings & Squad Rosters",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = TurfGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

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
                    text = { Text("Points Table & Standings", color = if (selectedTab == 0) TurfGreenPrimary else Color.White) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Squad Rosters", color = if (selectedTab == 1) TurfGreenPrimary else Color.White) }
                )
            }
        }

        if (selectedTab == 0) {
            // Points Table
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CricketNavy),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CricketGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Cross-Gaming Platform Championship",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CricketSurfaceDark)
                                .padding(vertical = 8.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("#", modifier = Modifier.weight(0.4f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CricketGold)
                            Text("Team", modifier = Modifier.weight(2.0f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("P", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                            Text("W", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight, textAlign = TextAlign.Center)
                            Text("L", modifier = Modifier.weight(0.5f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                            Text("NRR", modifier = Modifier.weight(0.9f), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.End)
                            Text("PTS", modifier = Modifier.weight(0.6f), fontSize = 11.sp, fontWeight = FontWeight.Black, color = CricketGold, textAlign = TextAlign.End)
                        }

                        HorizontalDivider(color = CricketNavyDark)

                        SampleStandings.forEach { standing ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${standing.rank}", modifier = Modifier.weight(0.4f), fontSize = 12.sp, color = if (standing.rank <= 2) CricketGold else Color.White)
                                Text(standing.team, modifier = Modifier.weight(2.0f), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("${standing.played}", modifier = Modifier.weight(0.5f), fontSize = 12.sp, color = Color.White, textAlign = TextAlign.Center)
                                Text("${standing.won}", modifier = Modifier.weight(0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight, textAlign = TextAlign.Center)
                                Text("${standing.lost}", modifier = Modifier.weight(0.5f), fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.Center)
                                Text(standing.nrr, modifier = Modifier.weight(0.9f), fontSize = 12.sp, color = Color.White, textAlign = TextAlign.End)
                                Text("${standing.points}", modifier = Modifier.weight(0.6f), fontSize = 14.sp, fontWeight = FontWeight.Black, color = CricketGold, textAlign = TextAlign.End)
                            }
                            HorizontalDivider(color = CricketSurfaceDark.copy(alpha = 0.4f))
                        }
                    }
                }
            }
        } else {
            // Squad Rosters
            items(SampleRosters) { roster ->
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
                                text = roster.teamName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CricketSurfaceDark)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = roster.league,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TurfGreenLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Captain: ${roster.captain}",
                            fontSize = 12.sp,
                            color = CricketGold
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Squad Roster:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            roster.starPlayers.forEach { player ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CricketSurfaceDark)
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = player.split(" ").last(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
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
