package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface CricketDao {

    // Matches
    @Query("SELECT * FROM matches ORDER BY uploadedAt DESC")
    fun getAllMatchesFlow(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :matchId LIMIT 1")
    suspend fun getMatchById(matchId: String): MatchEntity?

    @Query("SELECT * FROM matches WHERE title LIKE '%' || :query || '%' OR team1 LIKE '%' || :query || '%' OR team2 LIKE '%' || :query || '%'")
    fun searchMatches(query: String): Flow<List<MatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(matches: List<MatchEntity>)

    @Query("DELETE FROM matches WHERE id = :matchId")
    suspend fun deleteMatch(matchId: String)

    // Innings
    @Query("SELECT * FROM innings WHERE matchId = :matchId ORDER BY inningsNumber ASC")
    fun getInningsForMatch(matchId: String): Flow<List<InningsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInnings(innings: List<InningsEntity>)

    // Batting Records
    @Query("SELECT * FROM batting_records WHERE matchId = :matchId ORDER BY position ASC")
    fun getBattingRecordsForMatch(matchId: String): Flow<List<BattingRecordEntity>>

    @Query("SELECT * FROM batting_records WHERE inningsId = :inningsId ORDER BY position ASC")
    fun getBattingRecordsForInnings(inningsId: String): Flow<List<BattingRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBattingRecords(records: List<BattingRecordEntity>)

    // Bowling Records
    @Query("SELECT * FROM bowling_records WHERE matchId = :matchId ORDER BY wickets DESC, economy ASC")
    fun getBowlingRecordsForMatch(matchId: String): Flow<List<BowlingRecordEntity>>

    @Query("SELECT * FROM bowling_records WHERE inningsId = :inningsId")
    fun getBowlingRecordsForInnings(inningsId: String): Flow<List<BowlingRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBowlingRecords(records: List<BowlingRecordEntity>)

    // Partnerships & FOW
    @Query("SELECT * FROM partnerships WHERE inningsId = :inningsId ORDER BY wicketNumber ASC")
    fun getPartnershipsForInnings(inningsId: String): Flow<List<PartnershipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartnerships(partnerships: List<PartnershipEntity>)

    @Query("SELECT * FROM fall_of_wickets WHERE inningsId = :inningsId ORDER BY wicketNumber ASC")
    fun getFallOfWicketsForInnings(inningsId: String): Flow<List<FallOfWicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFallOfWickets(fow: List<FallOfWicketEntity>)

    // Player Profiles
    @Query("SELECT * FROM player_profiles ORDER BY runs DESC")
    fun getAllPlayersFlow(): Flow<List<PlayerProfileEntity>>

    @Query("SELECT * FROM player_profiles WHERE id = :playerId LIMIT 1")
    suspend fun getPlayerById(playerId: String): PlayerProfileEntity?

    @Query("SELECT * FROM player_profiles WHERE name LIKE '%' || :query || '%' OR team LIKE '%' || :query || '%'")
    fun searchPlayers(query: String): Flow<List<PlayerProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerProfileEntity>)

    @Update
    suspend fun updatePlayer(player: PlayerProfileEntity)

    // Upload Records
    @Query("SELECT * FROM upload_records ORDER BY uploadedAt DESC")
    fun getAllUploadRecordsFlow(): Flow<List<UploadRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUploadRecord(record: UploadRecordEntity)

    @Update
    suspend fun updateUploadRecord(record: UploadRecordEntity)

    @Query("DELETE FROM upload_records WHERE id = :uploadId")
    suspend fun deleteUploadRecord(uploadId: String)

    // Audit Logs
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAuditLogsFlow(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)

    // Daily Briefings
    @Query("SELECT * FROM daily_briefings ORDER BY date DESC")
    fun getAllBriefingsFlow(): Flow<List<DailyBriefingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBriefing(briefing: DailyBriefingEntity)

    // Aggregates & Counts
    @Query("SELECT COUNT(*) FROM matches")
    suspend fun getMatchCount(): Int

    @Query("SELECT COUNT(*) FROM player_profiles")
    suspend fun getPlayerCount(): Int

    @Query("SELECT COUNT(*) FROM upload_records")
    suspend fun getUploadCount(): Int

    @Transaction
    @Query("DELETE FROM matches")
    suspend fun clearAllMatches()
}
