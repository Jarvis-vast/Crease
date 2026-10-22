package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: String,
    val title: String,
    val tournament: String,
    val format: String, // T20, ODI, Test, 5-Over Blitz, 10-Over Blast
    val date: String,
    val venue: String,
    val team1: String,
    val team2: String,
    val tossResult: String,
    val status: String, // LIVE, COMPLETED, UPCOMING
    val resultText: String,
    val winnerTeam: String,
    val manOfTheMatch: String,
    val sourcePlatform: String, // Real Cricket 24, WCC 3, Stick Cricket, EA Cricket, Custom League, Official
    val isGaming: Boolean,
    val rawImageUri: String? = null,
    val uploadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "innings")
data class InningsEntity(
    @PrimaryKey val id: String,
    val matchId: String,
    val inningsNumber: Int, // 1 or 2
    val battingTeam: String,
    val bowlingTeam: String,
    val totalRuns: Int,
    val totalWickets: Int,
    val overs: Float,
    val extras: Int,
    val runRate: Float
)

@Entity(tableName = "batting_records")
data class BattingRecordEntity(
    @PrimaryKey val id: String,
    val inningsId: String,
    val matchId: String,
    val playerName: String,
    val team: String,
    val position: Int,
    val runs: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val strikeRate: Float,
    val dismissal: String,
    val isNotOut: Boolean,
    val fantasyPoints: Int = 0
)

@Entity(tableName = "bowling_records")
data class BowlingRecordEntity(
    @PrimaryKey val id: String,
    val inningsId: String,
    val matchId: String,
    val bowlerName: String,
    val team: String,
    val overs: Float,
    val maidens: Int,
    val runsConceded: Int,
    val wickets: Int,
    val economy: Float,
    val dots: Int = 0,
    val foursConceded: Int = 0,
    val sixesConceded: Int = 0,
    val fantasyPoints: Int = 0
)

@Entity(tableName = "partnerships")
data class PartnershipEntity(
    @PrimaryKey val id: String,
    val inningsId: String,
    val wicketNumber: Int,
    val player1Name: String,
    val player2Name: String,
    val runs: Int,
    val balls: Int
)

@Entity(tableName = "fall_of_wickets")
data class FallOfWicketEntity(
    @PrimaryKey val id: String,
    val inningsId: String,
    val wicketNumber: Int,
    val score: Int,
    val over: Float,
    val playerOut: String
)

@Entity(tableName = "player_profiles")
data class PlayerProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val team: String,
    val role: String, // Top-order Batter, All-rounder, Pace Bowler, Spin Bowler, Wicketkeeper
    val battingStyle: String,
    val bowlingStyle: String,
    val matches: Int,
    val innings: Int,
    val runs: Int,
    val highestScore: Int,
    val isHighestNotOut: Boolean,
    val battingAverage: Float,
    val strikeRate: Float,
    val hundreds: Int,
    val fifties: Int,
    val fours: Int,
    val sixes: Int,
    val wickets: Int,
    val bowlingAverage: Float,
    val economy: Float,
    val bestBowling: String,
    val fiveWickets: Int,
    val catches: Int,
    val recentScores: String, // comma separated e.g. "74, 52, 108*, 14, 45"
    val consistencyIndex: Float, // 0-100 score
    val sourcePlatform: String
)

@Entity(tableName = "upload_records")
data class UploadRecordEntity(
    @PrimaryKey val id: String,
    val fileName: String,
    val sourcePlatform: String,
    val uploadedAt: Long,
    val status: String, // PARSED, REVIEW_PENDING, CONFIRMED, ERROR
    val confidence: Float,
    val rawText: String,
    val parsedMatchId: String?,
    val issuesFound: String
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val action: String,
    val details: String,
    val user: String = "Platform Admin"
)

@Entity(tableName = "daily_briefings")
data class DailyBriefingEntity(
    @PrimaryKey val id: String,
    val date: String,
    val title: String,
    val summaryText: String,
    val topBatsman: String,
    val topBowler: String,
    val matchesCovered: Int,
    val sentStatus: String, // DELIVERED, SCHEDULED, TEST_SENT
    val deliveredAt: String
)
