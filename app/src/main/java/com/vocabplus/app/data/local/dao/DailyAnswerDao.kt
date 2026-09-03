package com.vocabplus.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vocabplus.app.data.local.entity.DailyAnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyAnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Safe idempotency: returns -1 if duplicate
    suspend fun insertAnswer(answer: DailyAnswerEntity): Long

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso AND category_slug = :categorySlug")
    suspend fun getAnswersForSection(dateIso: String, categorySlug: String): List<DailyAnswerEntity>

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso AND category_slug = :categorySlug")
    fun getAnswersForSectionFlow(dateIso: String, categorySlug: String): Flow<List<DailyAnswerEntity>>

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso")
    suspend fun getAllAnswersForDate(dateIso: String): List<DailyAnswerEntity>

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso AND question_id = :questionId")
    suspend fun getAnswer(dateIso: String, questionId: String): DailyAnswerEntity?
}