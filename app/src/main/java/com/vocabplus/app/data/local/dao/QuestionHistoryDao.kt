package com.vocabplus.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vocabplus.app.data.local.entity.QuestionHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(history: QuestionHistoryEntity)

    @Query("SELECT * FROM question_history WHERE question_id = :questionId")
    suspend fun getHistory(questionId: String): QuestionHistoryEntity?

    @Query("SELECT * FROM question_history")
    fun getAllHistoryFlow(): Flow<List<QuestionHistoryEntity>>

    @Query("SELECT * FROM question_history")
    suspend fun getAllHistory(): List<QuestionHistoryEntity>

    @Query("SELECT * FROM question_history WHERE next_revision_at_millis <= :currentMillis")
    suspend fun getEligibleForRevision(currentMillis: Long): List<QuestionHistoryEntity>
}