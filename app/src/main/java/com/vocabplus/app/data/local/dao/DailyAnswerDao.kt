package com.vocabplus.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vocabplus.app.data.local.entity.DailyAnswerEntity
import kotlinx.coroutines.flow.Flow

data class CategoryStatTuple(
    val category_slug: String,
    val total_answered: Int,
    val total_correct: Int
)

@Dao
interface DailyAnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnswer(answer: DailyAnswerEntity): Long

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso AND category_slug = :categorySlug")
    suspend fun getAnswersForSection(dateIso: String, categorySlug: String): List<DailyAnswerEntity>

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso AND category_slug = :categorySlug")
    fun getAnswersForSectionFlow(dateIso: String, categorySlug: String): Flow<List<DailyAnswerEntity>>

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso")
    suspend fun getAllAnswersForDate(dateIso: String): List<DailyAnswerEntity>

    @Query("SELECT * FROM daily_answers WHERE date_iso = :dateIso AND question_id = :questionId")
    suspend fun getAnswer(dateIso: String, questionId: String): DailyAnswerEntity?

    @Query("SELECT category_slug, COUNT(*) as total_answered, SUM(CASE WHEN is_correct THEN 1 ELSE 0 END) as total_correct FROM daily_answers GROUP BY category_slug")
    fun getCategoryStatsFlow(): Flow<List<CategoryStatTuple>>
}