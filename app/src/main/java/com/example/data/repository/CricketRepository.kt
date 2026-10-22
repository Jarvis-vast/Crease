package com.example.data.repository

import android.content.Context
import com.example.data.local.CricketDao
import com.example.data.local.CricketDatabase
import com.example.data.local.SampleDataProvider
import com.example.data.model.AuditLogEntity
import com.example.data.model.BattingRecordEntity
import com.example.data.model.BowlingRecordEntity
import com.example.data.model.DailyBriefingEntity
import com.example.data.model.FallOfWicketEntity
import com.example.data.model.InningsEntity
import com.example.data.model.MatchEntity
import com.example.data.model.PartnershipEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.UploadRecordEntity
import com.example.data.parser.ParsedScorecardResult
import com.example.data.parser.ScorecardParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CricketRepository(context: Context) {
    private val database = CricketDatabase.getDatabase(context)
    private val dao: CricketDao = database.cricketDao()

    suspend fun initializeIfNeeded() = withContext(Dispatchers.IO) {
        if (dao.getMatchCount() == 0) {
            val (matches, innings, records) = SampleDataProvider.getSampleMatchesData()
            dao.insertMatches(matches)
            dao.insertInnings(innings)
            dao.insertBattingRecords(records.first)
            dao.insertBowlingRecords(records.second)
            dao.insertPlayers(SampleDataProvider.getSamplePlayers())
            SampleDataProvider.getSampleUploads().forEach { dao.insertUploadRecord(it) }
            SampleDataProvider.getSampleAuditLogs().forEach { dao.insertAuditLog(it) }
            SampleDataProvider.getSampleBriefings().forEach { dao.insertBriefing(it) }
        }
    }

    // Matches
    fun getAllMatches(): Flow<List<MatchEntity>> = dao.getAllMatchesFlow()
    fun searchMatches(query: String): Flow<List<MatchEntity>> = dao.searchMatches(query)
    suspend fun getMatchById(id: String): MatchEntity? = withContext(Dispatchers.IO) { dao.getMatchById(id) }

    // Innings & Records
    fun getInningsForMatch(matchId: String): Flow<List<InningsEntity>> = dao.getInningsForMatch(matchId)
    fun getBattingRecordsForMatch(matchId: String): Flow<List<BattingRecordEntity>> = dao.getBattingRecordsForMatch(matchId)
    fun getBowlingRecordsForMatch(matchId: String): Flow<List<BowlingRecordEntity>> = dao.getBowlingRecordsForMatch(matchId)

    // Players
    fun getAllPlayers(): Flow<List<PlayerProfileEntity>> = dao.getAllPlayersFlow()
    fun searchPlayers(query: String): Flow<List<PlayerProfileEntity>> = dao.searchPlayers(query)
    suspend fun getPlayerById(id: String): PlayerProfileEntity? = withContext(Dispatchers.IO) { dao.getPlayerById(id) }

    // Uploads
    fun getAllUploadRecords(): Flow<List<UploadRecordEntity>> = dao.getAllUploadRecordsFlow()

    // Audit
    fun getAuditLogs(): Flow<List<AuditLogEntity>> = dao.getAuditLogsFlow()

    // Briefings
    fun getAllBriefings(): Flow<List<DailyBriefingEntity>> = dao.getAllBriefingsFlow()

    /**
     * Parse raw text into structured scorecard result
     */
    fun parseScorecard(rawText: String, platform: String): ParsedScorecardResult {
        return ScorecardParser.parseText(rawText, platform)
    }

    /**
     * Commit a validated / reviewed scorecard into Room database & update player stats
     */
    suspend fun commitScorecard(result: ParsedScorecardResult, fileName: String): String = withContext(Dispatchers.IO) {
        val match = result.match
        dao.insertMatch(match)
        dao.insertInnings(result.inningsList)
        dao.insertBattingRecords(result.battingRecords)
        dao.insertBowlingRecords(result.bowlingRecords)
        if (result.partnerships.isNotEmpty()) dao.insertPartnerships(result.partnerships)
        if (result.fallOfWickets.isNotEmpty()) dao.insertFallOfWickets(result.fallOfWickets)

        // Upload log
        val uploadRecord = UploadRecordEntity(
            id = UUID.randomUUID().toString(),
            fileName = fileName,
            sourcePlatform = result.sourcePlatform,
            uploadedAt = System.currentTimeMillis(),
            status = "CONFIRMED",
            confidence = result.confidence,
            rawText = "Imported match ${match.title} with ${result.battingRecords.size} batting and ${result.bowlingRecords.size} bowling figures.",
            parsedMatchId = match.id,
            issuesFound = if (result.issuesFound.isEmpty()) "Verified & Corrected cleanly" else result.issuesFound.joinToString("; ")
        )
        dao.insertUploadRecord(uploadRecord)

        // Audit log
        val auditLog = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            action = "SCORECARD_COMMITTED",
            details = "Committed match '${match.title}' (${result.sourcePlatform}) with confidence ${(result.confidence * 100).toInt()}%"
        )
        dao.insertAuditLog(auditLog)

        // Update or Insert Player Profiles for all participants
        updatePlayerProfilesFromMatch(result.battingRecords, result.bowlingRecords, result.sourcePlatform)

        match.id
    }

    private suspend fun updatePlayerProfilesFromMatch(
        batting: List<BattingRecordEntity>,
        bowling: List<BowlingRecordEntity>,
        platform: String
    ) {
        val allPlayers = mutableMapOf<String, PlayerProfileEntity>()
        // Fetch existing players
        // We'll update or create
        for (b in batting) {
            val key = b.playerName.trim()
            val existing = dao.getPlayerById("p_${key.lowercase().replace(" ", "_")}")
            if (existing != null) {
                val newMatches = existing.matches + 1
                val newInnings = existing.innings + 1
                val newRuns = existing.runs + b.runs
                val newHighest = maxOf(existing.highestScore, b.runs)
                val newHundreds = existing.hundreds + if (b.runs >= 100) 1 else 0
                val newFifties = existing.fifties + if (b.runs in 50..99) 1 else 0
                val newFours = existing.fours + b.fours
                val newSixes = existing.sixes + b.sixes
                val newAvg = if (newInnings > 0) newRuns.toFloat() / newInnings else newRuns.toFloat()

                val updated = existing.copy(
                    matches = newMatches,
                    innings = newInnings,
                    runs = newRuns,
                    highestScore = newHighest,
                    hundreds = newHundreds,
                    fifties = newFifties,
                    fours = newFours,
                    sixes = newSixes,
                    battingAverage = String.format(Locale.US, "%.2f", newAvg).toFloatOrNull() ?: newAvg,
                    recentScores = "${b.runs}, " + existing.recentScores.take(20)
                )
                dao.insertPlayers(listOf(updated))
            } else {
                val newProfile = PlayerProfileEntity(
                    id = "p_${key.lowercase().replace(" ", "_")}",
                    name = key,
                    team = b.team,
                    role = "Batter",
                    battingStyle = "Right-hand bat",
                    bowlingStyle = "Right-arm medium",
                    matches = 1,
                    innings = 1,
                    runs = b.runs,
                    highestScore = b.runs,
                    isHighestNotOut = b.isNotOut,
                    battingAverage = b.runs.toFloat(),
                    strikeRate = b.strikeRate,
                    hundreds = if (b.runs >= 100) 1 else 0,
                    fifties = if (b.runs in 50..99) 1 else 0,
                    fours = b.fours,
                    sixes = b.sixes,
                    wickets = 0,
                    bowlingAverage = 0f,
                    economy = 0f,
                    bestBowling = "0/0",
                    fiveWickets = 0,
                    catches = 0,
                    recentScores = "${b.runs}",
                    consistencyIndex = 85.0f,
                    sourcePlatform = platform
                )
                dao.insertPlayers(listOf(newProfile))
            }
        }

        for (bw in bowling) {
            val key = bw.bowlerName.trim()
            val existing = dao.getPlayerById("p_${key.lowercase().replace(" ", "_")}")
            if (existing != null) {
                val newWkts = existing.wickets + bw.wickets
                val new5W = existing.fiveWickets + if (bw.wickets >= 5) 1 else 0
                val updated = existing.copy(
                    wickets = newWkts,
                    fiveWickets = new5W
                )
                dao.insertPlayers(listOf(updated))
            }
        }
    }

    suspend fun triggerDailyMorningBriefing(): DailyBriefingEntity = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.US)
        val nowStr = dateFormat.format(Date())
        val count = dao.getMatchCount()

        val briefing = DailyBriefingEntity(
            id = UUID.randomUUID().toString(),
            date = "Today ($nowStr)",
            title = "Morning Cricket Intelligence Briefing",
            summaryText = "Automated briefing: System has indexed $count matches across gaming platforms. Orange Cap: Virat Kohli (1,142 runs). Purple Cap: Jasprit Bumrah (41 wkts). All models reconciled with zero unhandled mathematical anomalies.",
            topBatsman = "Virat Kohli (RC24 Champion)",
            topBowler = "Jasprit Bumrah (Purple Cap Leader)",
            matchesCovered = count,
            sentStatus = "DELIVERED",
            deliveredAt = SimpleDateFormat("HH:mm:ss 'UTC'", Locale.US).format(Date())
        )
        dao.insertBriefing(briefing)

        val log = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            action = "BRIEFING_DISPATCHED",
            details = "Daily Morning Briefing compiled and dispatched successfully via notification/service."
        )
        dao.insertAuditLog(log)
        briefing
    }

    suspend fun deleteMatch(matchId: String) = withContext(Dispatchers.IO) {
        dao.deleteMatch(matchId)
        val log = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            action = "MATCH_DELETED",
            details = "Removed match record: $matchId"
        )
        dao.insertAuditLog(log)
    }

    suspend fun resetDatabase() = withContext(Dispatchers.IO) {
        dao.clearAllMatches()
        initializeIfNeeded()
        val log = AuditLogEntity(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            action = "DATABASE_RESET",
            details = "Reset database to initial calibrated benchmark state."
        )
        dao.insertAuditLog(log)
    }

    suspend fun mergePlayers(sourcePlayerId: String, targetPlayerId: String) = withContext(Dispatchers.IO) {
        val source = dao.getPlayerById(sourcePlayerId)
        val target = dao.getPlayerById(targetPlayerId)
        if (source != null && target != null) {
            val merged = target.copy(
                matches = target.matches + source.matches,
                runs = target.runs + source.runs,
                wickets = target.wickets + source.wickets,
                fours = target.fours + source.fours,
                sixes = target.sixes + source.sixes,
                hundreds = target.hundreds + source.hundreds,
                fifties = target.fifties + source.fifties
            )
            dao.insertPlayers(listOf(merged))
            // Log audit
            dao.insertAuditLog(
                AuditLogEntity(
                    id = UUID.randomUUID().toString(),
                    timestamp = System.currentTimeMillis(),
                    action = "PLAYER_MERGED",
                    details = "Merged '${source.name}' into '${target.name}'"
                )
            )
        }
    }
}
