package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        MatchEntity::class,
        InningsEntity::class,
        BattingRecordEntity::class,
        BowlingRecordEntity::class,
        PartnershipEntity::class,
        FallOfWicketEntity::class,
        PlayerProfileEntity::class,
        UploadRecordEntity::class,
        AuditLogEntity::class,
        DailyBriefingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CricketDatabase : RoomDatabase() {
    abstract fun cricketDao(): CricketDao

    companion object {
        @Volatile
        private var INSTANCE: CricketDatabase? = null

        fun getDatabase(context: Context): CricketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CricketDatabase::class.java,
                    "cricket_intel_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
