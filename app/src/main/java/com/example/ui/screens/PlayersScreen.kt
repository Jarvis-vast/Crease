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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.PlayerProfileEntity
import com.example.ui.CricketViewModel
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

@Composable
fun PlayersScreen(
    viewModel: CricketViewModel
) {
    val players by viewModel.filteredPlayers.collectAsState()
    var selectedRoleFilter by remember { mutableStateOf("All") }
    var viewingPlayer by remember { mutableStateOf<PlayerProfileEntity?>(null) }

    val roleFilters = listOf("All", "Top-order Batter", "All-rounder", "Pace Bowler", "Spin Bowler")

    val displayedPlayers = players.filter {
        selectedRoleFilter == "All" || it.role.contains(selectedRoleFilter, ignoreCase = true)
    }

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
                        text = "Player Intelligence Profiles",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "${displayedPlayers.size} Profiles Tracked • Auto-Aggregated from Scorecards",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Role Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(roleFilters) { role ->
                    val isSelected = selectedRoleFilter == role
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedRoleFilter = role },
                        label = { Text(role, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TurfGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = CricketNavy,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("filter_role_${role.lowercase().replace(" ", "_")}")
                    )
                }
            }
        }

        items(displayedPlayers) { player ->
            PlayerCard(
                player = player,
                onClick = { viewingPlayer = player }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Player Detail Modal
    viewingPlayer?.let { player ->
        PlayerDetailDialog(player = player, onDismiss = { viewingPlayer = null })
    }
}

@Composable
fun PlayerCard(
    player: PlayerProfileEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CricketNavy),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("player_card_${player.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CricketSurfaceDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = TurfGreenPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = player.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CricketSurfaceDark)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CI: ${player.consistencyIndex.toInt()}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CricketGold
                            )
                        }
                    }
                    Text(
                        text = "${player.team} • ${player.role}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CricketSurfaceDark)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatColumn(label = "Matches", value = "${player.matches}")
                StatColumn(label = "Runs", value = "${player.runs}", highlightColor = CricketGold)
                StatColumn(label = "Average", value = "${player.battingAverage}")
                StatColumn(label = "Strike Rate", value = "${player.strikeRate}", highlightColor = TurfGreenLight)
                StatColumn(label = "Wickets", value = "${player.wickets}")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Form: ${player.recentScores}",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = player.sourcePlatform,
                    fontSize = 10.sp,
                    color = TurfGreenLight
                )
            }
        }
    }
}

@Composable
fun StatColumn(
    label: String,
    value: String,
    highlightColor: Color = Color.White
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = highlightColor)
    }
}

@Composable
fun PlayerDetailDialog(
    player: PlayerProfileEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CricketNavy,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = player.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "${player.team} • ${player.role}",
                        fontSize = 12.sp,
                        color = CricketGold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CricketSurfaceDark)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Style: ${player.battingStyle} | ${player.bowlingStyle}", fontSize = 12.sp, color = Color.White)
                        Text("Highest Score: ${player.highestScore} • 100s: ${player.hundreds} • 50s: ${player.fifties}", fontSize = 12.sp, color = TurfGreenLight)
                        Text("Best Bowling: ${player.bestBowling} • 5-Wickets: ${player.fiveWickets} • Catches: ${player.catches}", fontSize = 12.sp, color = CricketGold)
                        Text("Consistency Index: ${player.consistencyIndex}%", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                }

                Text(
                    text = "Recent Scores Trajectory:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    player.recentScores.split(",").forEach { scoreStr ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CricketNavyDark)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = scoreStr.trim(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TurfGreenLight
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TurfGreenPrimary, fontWeight = FontWeight.Bold)
            }
        }
    )
}
