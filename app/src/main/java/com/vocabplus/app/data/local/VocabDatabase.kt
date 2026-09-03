package com.vocabplus.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vocabplus.app.data.local.dao.DailyAnswerDao
import com.vocabplus.app.data.local.dao.DailySectionDao
import com.vocabplus.app.data.local.dao.QuestionHistoryDao
import com.vocabplus.app.data.local.dao.UserStatsDao
import com.vocabplus.app.data.local.entity.DailyAnswerEntity
import com.vocabplus.app.data.local.entity.DailySectionEntity
import com.vocabplus.app.data.local.entity.QuestionHistoryEntity
import com.vocabplus.app.data.local.entity.UserStatsEntity

@Database(
    entities = [
        UserStatsEntity::class,
        DailyAnswerEntity::class,
        DailySectionEntity::class,
        QuestionHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VocabDatabase : RoomDatabase() {

    abstract fun userStatsDao(): UserStatsDao
    abstract fun dailyAnswerDao(): DailyAnswerDao
    abstract fun dailySectionDao(): DailySectionDao
    abstract fun questionHistoryDao(): QuestionHistoryDao

    companion object {
        private const val DATABASE_NAME = "vocab_plus.db"

        @Volatile
        private var INSTANCE: VocabDatabase? = null

        fun getInstance(context: Context): VocabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VocabDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}