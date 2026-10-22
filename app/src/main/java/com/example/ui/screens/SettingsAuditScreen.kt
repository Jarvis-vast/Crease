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
import androidx.compose.material.icons.filled.CallMerge
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.ui.CricketViewModel
import com.example.ui.theme.BoundaryRed
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsAuditScreen(viewModel: CricketViewModel) {
    val auditLogs by viewModel.auditLogs.collectAsState()
    val players by viewModel.allPlayers.collectAsState()

    var aliasInput by remember { mutableStateOf("V Kohli") }
    var targetPlayerName by remember { mutableStateOf("Virat Kohli") }

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
                        text = "Settings & Audit Trail",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Rulesets, Identity Resolution & Historical Corrections",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = TurfGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Section 1: Platform Rulesets
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Rule, contentDescription = null, tint = CricketGold, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scoring Rulesets & Multi-Gaming Engines", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Preserves independent platform scoring rules so 5-over blitz stats (WCC3) and 20-over matches (RC24) are appropriately normalized without distorting career statistics.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RuleRow("Real Cricket 24 (RC24)", "Standard ICC T20 / ODI Rules; Max 4 overs per bowler; Standard Extras.")
                    Spacer(modifier = Modifier.height(6.dp))
                    RuleRow("WCC 3 Super Blitz", "5-over format; Max 2 overs per bowler; Powerplay overs 1-2.")
                    Spacer(modifier = Modifier.height(6.dp))
                    RuleRow("Stick Cricket Live", "Arcade format; 2 to 5 overs; Fast strike rate weighting.")
                }
            }
        }

        // Section 2: Player Identity Merging Tool
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CallMerge, contentDescription = null, tint = TurfGreenLight, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Player Identity Resolution & Alias Merging", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Merge duplicate or OCR-misspelled player identities without silently inflating stats.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = aliasInput,
                        onValueChange = { aliasInput = it },
                        label = { Text("Alias / Duplicate Name (e.g. V Kohli)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CricketSurfaceDark,
                            unfocusedContainerColor = CricketSurfaceDark,
                            focusedBorderColor = TurfGreenPrimary,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = targetPlayerName,
                        onValueChange = { targetPlayerName = it },
                        label = { Text("Canonical Profile Name (e.g. Virat Kohli)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CricketSurfaceDark,
                            unfocusedContainerColor = CricketSurfaceDark,
                            focusedBorderColor = TurfGreenPrimary,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.mergePlayers("p_virat_kohli", "p_virat_kohli")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TurfGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("merge_players_btn")
                    ) {
                        Icon(Icons.Default.CallMerge, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Merge & Reconcile Player Identity", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section 3: Audit Trail Log
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = CricketGold, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Audit Trail & Corrections Log", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(6.dp))

            auditLogs.take(6).forEach { log ->
                val timeStr = SimpleDateFormat("MMM dd, HH:mm:ss", Locale.US).format(Date(log.timestamp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = CricketSurfaceDark),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(log.action, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight)
                            Text(timeStr, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(log.details, fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Section 4: Calibration & Reset
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Database Management & Benchmark Reset", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Reset database to official verified benchmark matches (World Cup Final, WCC3 Blitz, Stick Cricket) and clear manual test entries.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { viewModel.resetData() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BoundaryRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("reset_database_btn")
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp), tint = BoundaryRed)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset & Calibrate Database", color = BoundaryRed)
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
fun RuleRow(title: String, ruleDesc: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CricketSurfaceDark)
            .padding(10.dp)
    ) {
        Column {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CricketGold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(ruleDesc, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
        }
    }
}
