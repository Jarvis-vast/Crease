package com.example.data.local

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
import java.util.UUID

object SampleDataProvider {

    fun getSamplePlayers(): List<PlayerProfileEntity> {
        return listOf(
            PlayerProfileEntity(
                id = "p_virat_kohli",
                name = "Virat Kohli",
                team = "Royal Challengers / India",
                role = "Top-order Batter",
                battingStyle = "Right-hand bat",
                bowlingStyle = "Right-arm medium",
                matches = 28,
                innings = 27,
                runs = 1142,
                highestScore = 113,
                isHighestNotOut = true,
                battingAverage = 54.38f,
                strikeRate = 148.65f,
                hundreds = 3,
                fifties = 8,
                fours = 104,
                sixes = 42,
                wickets = 2,
                bowlingAverage = 32.0f,
                economy = 8.5f,
                bestBowling = "2/16",
                fiveWickets = 0,
                catches = 18,
                recentScores = "76*, 82, 38*, 54, 92*",
                consistencyIndex = 94.2f,
                sourcePlatform = "Real Cricket 24"
            ),
            PlayerProfileEntity(
                id = "p_rohit_sharma",
                name = "Rohit Sharma",
                team = "Mumbai Titans / India",
                role = "Top-order Batter",
                battingStyle = "Right-hand bat",
                bowlingStyle = "Right-arm offbreak",
                matches = 26,
                innings = 26,
                runs = 988,
                highestScore = 105,
                isHighestNotOut = false,
                battingAverage = 41.16f,
                strikeRate = 162.40f,
                hundreds = 2,
                fifties = 7,
                fours = 96,
                sixes = 51,
                wickets = 0,
                bowlingAverage = 0f,
                economy = 9.2f,
                bestBowling = "0/18",
                fiveWickets = 0,
                catches = 14,
                recentScores = "47, 68, 31, 89, 24",
                consistencyIndex = 88.5f,
                sourcePlatform = "Real Cricket 24"
            ),
            PlayerProfileEntity(
                id = "p_jasprit_bumrah",
                name = "Jasprit Bumrah",
                team = "Mumbai Titans / India",
                role = "Pace Bowler",
                battingStyle = "Right-hand bat",
                bowlingStyle = "Right-arm fast",
                matches = 25,
                innings = 8,
                runs = 54,
                highestScore = 18,
                isHighestNotOut = true,
                battingAverage = 13.50f,
                strikeRate = 112.50f,
                hundreds = 0,
                fifties = 0,
                fours = 4,
                sixes = 2,
                wickets = 41,
                bowlingAverage = 14.82f,
                economy = 6.12f,
                bestBowling = "5/10",
                fiveWickets = 2,
                catches = 8,
                recentScores = "2/24, 3/14, 1/18, 4/19, 2/12",
                consistencyIndex = 96.8f,
                sourcePlatform = "Real Cricket 24"
            ),
            PlayerProfileEntity(
                id = "p_glenn_maxwell",
                name = "Glenn Maxwell",
                team = "Royal Challengers / Australia",
                role = "All-rounder",
                battingStyle = "Right-hand bat",
                bowlingStyle = "Right-arm offbreak",
                matches = 24,
                innings = 23,
                runs = 724,
                highestScore = 106,
                isHighestNotOut = true,
                battingAverage = 36.20f,
                strikeRate = 184.22f,
                hundreds = 1,
                fifties = 5,
                fours = 62,
                sixes = 46,
                wickets = 18,
                bowlingAverage = 22.4f,
                economy = 8.14f,
                bestBowling = "3/22",
                fiveWickets = 0,
                catches = 16,
                recentScores = "44, 12, 68*, 22, 106*",
                consistencyIndex = 82.0f,
                sourcePlatform = "Stick Cricket Live"
            ),
            PlayerProfileEntity(
                id = "p_travis_head",
                name = "Travis Head",
                team = "Sunrisers / Australia",
                role = "Top-order Batter",
                battingStyle = "Left-hand bat",
                bowlingStyle = "Right-arm offbreak",
                matches = 22,
                innings = 22,
                runs = 874,
                highestScore = 102,
                isHighestNotOut = false,
                battingAverage = 43.70f,
                strikeRate = 191.24f,
                hundreds = 2,
                fifties = 6,
                fours = 98,
                sixes = 48,
                wickets = 4,
                bowlingAverage = 28.5f,
                economy = 9.40f,
                bestBowling = "2/19",
                fiveWickets = 0,
                catches = 9,
                recentScores = "38, 89, 102, 24, 76",
                consistencyIndex = 89.1f,
                sourcePlatform = "WCC 3"
            ),
            PlayerProfileEntity(
                id = "p_rashid_khan",
                name = "Rashid Khan",
                team = "Gujarat Titans / Afghanistan",
                role = "All-rounder",
                battingStyle = "Right-hand bat",
                bowlingStyle = "Right-arm legbreak",
                matches = 27,
                innings = 16,
                runs = 265,
                highestScore = 48,
                isHighestNotOut = true,
                battingAverage = 24.09f,
                strikeRate = 175.50f,
                hundreds = 0,
                fifties = 0,
                fours = 22,
                sixes = 18,
                wickets = 38,
                bowlingAverage = 17.15f,
                economy = 6.45f,
                bestBowling = "4/16",
                fiveWickets = 0,
                catches = 15,
                recentScores = "3/18, 2/22, 1/26, 4/16, 2/20",
                consistencyIndex = 93.4f,
                sourcePlatform = "Real Cricket 24"
            )
        )
    }

    fun getSampleMatchesData(): Triple<List<MatchEntity>, List<InningsEntity>, Pair<List<BattingRecordEntity>, List<BowlingRecordEntity>>> {
        val match1Id = "match_rc24_wc_final"
        val m1Innings1Id = "inn_rc24_1"
        val m1Innings2Id = "inn_rc24_2"

        val match1 = MatchEntity(
            id = match1Id,
            title = "India vs Australia",
            tournament = "Gaming World Cup 2024",
            format = "T20",
            date = "Oct 22, 2026",
            venue = "Narendra Modi Stadium (RC24 Arena)",
            team1 = "India",
            team2 = "Australia",
            tossResult = "India won toss & elected to bat",
            status = "COMPLETED",
            resultText = "India won by 18 runs",
            winnerTeam = "India",
            manOfTheMatch = "Virat Kohli",
            sourcePlatform = "Real Cricket 24",
            isGaming = true
        )

        val inn1 = InningsEntity(
            id = m1Innings1Id,
            matchId = match1Id,
            inningsNumber = 1,
            battingTeam = "India",
            bowlingTeam = "Australia",
            totalRuns = 202,
            totalWickets = 3,
            overs = 20.0f,
            extras = 6,
            runRate = 10.10f
        )

        val inn2 = InningsEntity(
            id = m1Innings2Id,
            matchId = match1Id,
            inningsNumber = 2,
            battingTeam = "Australia",
            bowlingTeam = "India",
            totalRuns = 184,
            totalWickets = 5,
            overs = 20.0f,
            extras = 4,
            runRate = 9.20f
        )

        val batting1 = listOf(
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Rohit Sharma", "India", 1, 47, 24, 6, 2, 195.83f, "c Maxwell b Starc", false, 65),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Virat Kohli", "India", 2, 76, 52, 6, 2, 146.15f, "not out", true, 114),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Rishabh Pant", "India", 3, 21, 14, 2, 1, 150.00f, "c Marsh b Cummins", false, 32),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Suryakumar Yadav", "India", 4, 34, 18, 3, 2, 188.89f, "c Smith b Zampa", false, 51),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Hardik Pandya", "India", 5, 24, 12, 1, 2, 200.00f, "not out", true, 38)
        )

        val batting2 = listOf(
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Travis Head", "Australia", 1, 38, 22, 5, 1, 172.72f, "b Bumrah", false, 52),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "David Warner", "Australia", 2, 18, 12, 3, 0, 150.00f, "c Pant b Arshdeep", false, 25),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Mitchell Marsh", "Australia", 3, 28, 19, 2, 1, 147.36f, "c Kohli b Kuldeep", false, 39),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Glenn Maxwell", "Australia", 4, 44, 26, 4, 2, 169.23f, "b Bumrah", false, 62),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Marcus Stoinis", "Australia", 5, 22, 14, 2, 1, 157.14f, "c Jadeja b Pandya", false, 31),
            BattingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Pat Cummins", "Australia", 6, 16, 9, 1, 1, 177.77f, "not out", true, 26)
        )

        val bowling1 = listOf(
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Mitchell Starc", "Australia", 4.0f, 0, 42, 1, 10.50f, 8, 4, 2, 28),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Pat Cummins", "Australia", 4.0f, 0, 36, 1, 9.00f, 10, 3, 1, 31),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Josh Hazlewood", "Australia", 4.0f, 0, 38, 0, 9.50f, 9, 4, 1, 12),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Adam Zampa", "Australia", 4.0f, 0, 44, 1, 11.00f, 7, 3, 2, 27),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings1Id, match1Id, "Glenn Maxwell", "Australia", 4.0f, 0, 42, 0, 10.50f, 6, 3, 2, 10)
        )

        val bowling2 = listOf(
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Jasprit Bumrah", "India", 4.0f, 1, 24, 2, 6.00f, 16, 2, 0, 78),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Arshdeep Singh", "India", 4.0f, 0, 38, 1, 9.50f, 9, 3, 1, 30),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Kuldeep Yadav", "India", 4.0f, 0, 32, 1, 8.00f, 11, 2, 1, 34),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Hardik Pandya", "India", 4.0f, 0, 44, 1, 11.00f, 7, 4, 2, 26),
            BowlingRecordEntity(UUID.randomUUID().toString(), m1Innings2Id, match1Id, "Ravindra Jadeja", "India", 4.0f, 0, 46, 0, 11.50f, 6, 4, 1, 8)
        )

        // Match 2: WCC 3 Super Blitz
        val match2Id = "match_wcc3_blitz_1"
        val m2Innings1Id = "inn_wcc3_1"
        val m2Innings2Id = "inn_wcc3_2"

        val match2 = MatchEntity(
            id = match2Id,
            title = "Royal Challengers vs Mumbai Titans",
            tournament = "WCC 3 Super Blitz League",
            format = "5-Over Blitz",
            date = "Oct 21, 2026",
            venue = "Wankhede Arena (WCC 3)",
            team1 = "Royal Challengers",
            team2 = "Mumbai Titans",
            tossResult = "Mumbai Titans elected to bowl",
            status = "COMPLETED",
            resultText = "Royal Challengers won by 7 runs",
            winnerTeam = "Royal Challengers",
            manOfTheMatch = "Virat Kohli",
            sourcePlatform = "WCC 3",
            isGaming = true
        )

        val m2Inn1 = InningsEntity(
            id = m2Innings1Id,
            matchId = match2Id,
            inningsNumber = 1,
            battingTeam = "Royal Challengers",
            bowlingTeam = "Mumbai Titans",
            totalRuns = 85,
            totalWickets = 1,
            overs = 5.0f,
            extras = 3,
            runRate = 17.0f
        )

        val m2Inn2 = InningsEntity(
            id = m2Innings2Id,
            matchId = match2Id,
            inningsNumber = 2,
            battingTeam = "Mumbai Titans",
            bowlingTeam = "Royal Challengers",
            totalRuns = 78,
            totalWickets = 2,
            overs = 5.0f,
            extras = 4,
            runRate = 15.6f
        )

        val m2Batting = listOf(
            BattingRecordEntity(UUID.randomUUID().toString(), m2Innings1Id, match2Id, "Virat Kohli", "Royal Challengers", 1, 38, 14, 4, 3, 271.42f, "not out", true, 68),
            BattingRecordEntity(UUID.randomUUID().toString(), m2Innings1Id, match2Id, "Faf du Plessis", "Royal Challengers", 2, 22, 8, 3, 1, 275.00f, "c Rohit b Bumrah", false, 36),
            BattingRecordEntity(UUID.randomUUID().toString(), m2Innings1Id, match2Id, "Glenn Maxwell", "Royal Challengers", 3, 25, 8, 2, 2, 312.50f, "not out", true, 42),
            BattingRecordEntity(UUID.randomUUID().toString(), m2Innings2Id, match2Id, "Rohit Sharma", "Mumbai Titans", 1, 31, 12, 4, 2, 258.33f, "c Maxwell b Siraj", false, 52),
            BattingRecordEntity(UUID.randomUUID().toString(), m2Innings2Id, match2Id, "Ishan Kishan", "Mumbai Titans", 2, 14, 6, 2, 1, 233.33f, "b Siraj", false, 24),
            BattingRecordEntity(UUID.randomUUID().toString(), m2Innings2Id, match2Id, "Suryakumar Yadav", "Mumbai Titans", 3, 29, 12, 3, 2, 241.66f, "not out", true, 48)
        )

        val m2Bowling = listOf(
            BowlingRecordEntity(UUID.randomUUID().toString(), m2Innings1Id, match2Id, "Jasprit Bumrah", "Mumbai Titans", 1.0f, 0, 14, 1, 14.0f, 2, 2, 0, 26),
            BowlingRecordEntity(UUID.randomUUID().toString(), m2Innings1Id, match2Id, "Trent Boult", "Mumbai Titans", 2.0f, 0, 32, 0, 16.0f, 3, 3, 2, 4),
            BowlingRecordEntity(UUID.randomUUID().toString(), m2Innings2Id, match2Id, "Mohammed Siraj", "Royal Challengers", 2.0f, 0, 26, 2, 13.0f, 4, 3, 1, 52)
        )

        val allMatches = listOf(match1, match2)
        val allInnings = listOf(inn1, inn2, m2Inn1, m2Inn2)
        val allBatting = batting1 + batting2 + m2Batting
        val allBowling = bowling1 + bowling2 + m2Bowling

        return Triple(allMatches, allInnings, Pair(allBatting, allBowling))
    }

    fun getSampleUploads(): List<UploadRecordEntity> {
        return listOf(
            UploadRecordEntity(
                id = "up_001",
                fileName = "rc24_worldcup_final_scorecard.png",
                sourcePlatform = "Real Cricket 24",
                uploadedAt = System.currentTimeMillis() - 86400000L * 2,
                status = "CONFIRMED",
                confidence = 0.98f,
                rawText = "RC24 Extracted: India 202/3 vs Australia 184/5",
                parsedMatchId = "match_rc24_wc_final",
                issuesFound = "None. All mathematical sums matched (confidence 98%)."
            ),
            UploadRecordEntity(
                id = "up_002",
                fileName = "wcc3_super_blitz_rd4.jpg",
                sourcePlatform = "WCC 3",
                uploadedAt = System.currentTimeMillis() - 86400000L,
                status = "CONFIRMED",
                confidence = 0.94f,
                rawText = "WCC3 Extracted: Royal Challengers 85/1 vs Mumbai Titans 78/2",
                parsedMatchId = "match_wcc3_blitz_1",
                issuesFound = "5-over format detected. Overs adjusted."
            ),
            UploadRecordEntity(
                id = "up_003",
                fileName = "stick_cricket_tournament_qf.csv",
                sourcePlatform = "Stick Cricket Live",
                uploadedAt = System.currentTimeMillis() - 3600000L * 5,
                status = "REVIEW_PENDING",
                confidence = 0.86f,
                rawText = "Stick Cricket CSV raw table: Chennai Kings 196/2 vs Kolkata Knights 182/4",
                parsedMatchId = null,
                issuesFound = "Bowler maidens field was empty; marked for user review."
            )
        )
    }

    fun getSampleAuditLogs(): List<AuditLogEntity> {
        return listOf(
            AuditLogEntity(
                id = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis() - 3600000L * 2,
                action = "VALIDATION_CONFIRMED",
                details = "Validated and merged match scorecard: India vs Australia (RC24)"
            ),
            AuditLogEntity(
                id = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis() - 3600000L * 12,
                action = "IDENTITY_MERGE",
                details = "Merged alias 'V Kohli' into 'Virat Kohli' profile"
            ),
            AuditLogEntity(
                id = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis() - 86400000L,
                action = "SCORECARD_IMPORTED",
                details = "Imported WCC 3 5-over blitz match: Royal Challengers vs Mumbai Titans"
            )
        )
    }

    fun getSampleBriefings(): List<DailyBriefingEntity> {
        return listOf(
            DailyBriefingEntity(
                id = "brief_001",
                date = "Today, 08:00 AM",
                title = "Cricket Intelligence Morning Briefing",
                summaryText = "Morning digest: 2 matches processed with 96% avg confidence. Virat Kohli leads Orange Cap with 1,142 runs (SR 148.65). Jasprit Bumrah holds Purple Cap with 41 wickets (Econ 6.12). 1 new upload pending review in upload queue.",
                topBatsman = "Virat Kohli (76* runs, RC24 Final)",
                topBowler = "Jasprit Bumrah (2/24, Econ 6.00)",
                matchesCovered = 2,
                sentStatus = "DELIVERED",
                deliveredAt = "08:00:15 UTC"
            )
        )
    }
}
