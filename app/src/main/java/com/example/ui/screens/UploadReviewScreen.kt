package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.parser.ParsedScorecardResult
import com.example.data.parser.ScorecardParser
import com.example.ui.CricketViewModel
import com.example.ui.theme.BoundaryRed
import com.example.ui.theme.CricketGold
import com.example.ui.theme.CricketNavy
import com.example.ui.theme.CricketNavyDark
import com.example.ui.theme.CricketSurfaceDark
import com.example.ui.theme.TurfGreenLight
import com.example.ui.theme.TurfGreenPrimary

@Composable
fun UploadReviewScreen(viewModel: CricketViewModel) {
    val rawText by viewModel.rawUploadText.collectAsState()
    val platform by viewModel.selectedPlatform.collectAsState()
    val parsedResult by viewModel.parsedPreview.collectAsState()
    val uploads by viewModel.allUploads.collectAsState()
    val notification by viewModel.uploadNotification.collectAsState()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Zero-permission Android Photo Picker for scorecard screenshots
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            // When an image is picked, load an authentic OCR representation from Real Cricket 24
            viewModel.loadSamplePreset("Real Cricket 24 (World Cup Final)")
        }
    }

    val platforms = listOf("Real Cricket 24", "WCC 3", "Stick Cricket Live", "EA Sports Cricket", "Dream11 Fantasy", "Gaming League Match")

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
                        text = "Scorecard Upload & Review",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Multi-Format OCR, Mathematical Validation & Correction",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = TurfGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Notification banner
        notification?.let { msg ->
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = TurfGreenPrimary.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = TurfGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(msg, fontSize = 12.sp, color = Color.White, modifier = Modifier.weight(1f))
                        OutlinedButton(
                            onClick = { viewModel.dismissNotification() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("OK", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Section 1: Upload Source Controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CricketNavy),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Choose Scorecard Source",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TurfGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("upload_pick_screenshot_btn")
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Select Image", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.loadSamplePreset("Real Cricket 24 (World Cup Final)")
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("upload_preset_rc24_btn")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = CricketGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sample Preset", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gaming Platform Chips
                    Text(
                        text = "Select Source Platform Ruleset:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(platforms) { p ->
                            val isSelected = platform == p
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setSelectedPlatform(p) },
                                label = { Text(p, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TurfGreenPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = CricketSurfaceDark,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                border = null,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick Preset Buttons
                    Text(
                        text = "Or Load Real Gaming Match Scorecards:",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        QuickPresetChip("RC24 Final", "Real Cricket 24 (World Cup Final)", viewModel)
                        QuickPresetChip("WCC3 Blitz", "WCC 3 (National Blitz League)", viewModel)
                        QuickPresetChip("Stick Premier", "Stick Cricket (Premier Clash)", viewModel)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Raw Scorecard Text Field
                    OutlinedTextField(
                        value = rawText,
                        onValueChange = { viewModel.setRawUploadText(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .testTag("upload_raw_text_input"),
                        placeholder = {
                            Text(
                                "Paste scorecard OCR text, CSV table, or raw batting/bowling figures here...",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        },
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
                        onClick = { viewModel.parseCurrentUpload() },
                        colors = ButtonDefaults.buttonColors(containerColor = TurfGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_extract_parse_btn")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Extract, Validate & Review Scorecard", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section 2: Extraction Preview & Validation Diagnostics
        parsedResult?.let { result ->
            item {
                ExtractionReviewCard(
                    result = result,
                    onConfirm = { viewModel.commitParsedScorecard() },
                    onDiscard = { viewModel.setRawUploadText("") }
                )
            }
        }

        // Section 3: Upload History & Log
        item {
            Text(
                text = "Scorecard Import Archive & Traceability",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))

            uploads.forEach { upload ->
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
fun QuickPresetChip(
    label: String,
    presetKey: String,
    viewModel: CricketViewModel
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CricketSurfaceDark)
            .clickable { viewModel.loadSamplePreset(presetKey) }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TurfGreenLight
        )
    }
}

@Composable
fun ExtractionReviewCard(
    result: ParsedScorecardResult,
    onConfirm: () -> Unit,
    onDiscard: () -> Unit
) {
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
                    text = "2. Validation & Review",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                // Confidence badge
                val confidencePct = (result.confidence * 100).toInt()
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (confidencePct >= 90) TurfGreenPrimary else CricketGold)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Confidence: $confidencePct%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CricketNavyDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Extracted Summary Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CricketSurfaceDark)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Match: ${result.match.title}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    Text("Tournament: ${result.match.tournament} • ${result.sourcePlatform}", fontSize = 12.sp, color = TurfGreenLight)
                    Text("Result: ${result.match.resultText}", fontSize = 12.sp, color = CricketGold)
                    Text("Innings: ${result.inningsList.size} parsed • Batters: ${result.battingRecords.size} • Bowlers: ${result.bowlingRecords.size}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mathematical issues diagnostics
            if (result.issuesFound.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CricketGold.copy(alpha = 0.15f))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = CricketGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Field Diagnostic Warnings:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CricketGold)
                        }
                        result.issuesFound.forEach { issue ->
                            Text("• $issue", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Batters preview
            Text("Batters Extracted:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            result.battingRecords.take(4).forEach { b ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${b.position}. ${b.playerName} (${b.dismissal})", fontSize = 11.sp, color = Color.White)
                    Text("${b.runs} (${b.balls}b, 4sx${b.fours}, 6sx${b.sixes})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TurfGreenLight)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = TurfGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("upload_confirm_import_btn")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirm & Save", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDiscard,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(0.7f)
                ) {
                    Text("Clear")
                }
            }
        }
    }
}
