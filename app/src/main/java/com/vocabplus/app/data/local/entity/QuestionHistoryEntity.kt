package com.vocabplus.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vocabplus.app.domain.model.QuestionHistory

@Entity(tableName = "question_history")
data class QuestionHistoryEntity(
    @PrimaryKey @ColumnInfo(name = "question_id") val questionId: String,
    @ColumnInfo(name = "first_seen_at_millis") val firstSeenAtMillis: Long,
    @ColumnInfo(name = "last_seen_at_millis") val lastSeenAtMillis: Long,
    @ColumnInfo(name = "times_seen") val timesSeen: Int = 0,
    @ColumnInfo(name = "times_correct") val timesCorrect: Int = 0,
    @ColumnInfo(name = "times_incorrect") val timesIncorrect: Int = 0,
    @ColumnInfo(name = "last_result_correct") val lastResultCorrect: Boolean = false,
    @ColumnInfo(name = "next_revision_at_millis") val nextRevisionAtMillis: Long = 0L
) {
    fun toDomain(): QuestionHistory = QuestionHistory(
        questionId = questionId,
        firstSeenAtMillis = firstSeenAtMillis,
        lastSeenAtMillis = lastSeenAtMillis,
        timesSeen = timesSeen,
        timesCorrect = timesCorrect,
        timesIncorrect = timesIncorrect,
        lastResultCorrect = lastResultCorrect,
        nextRevisionAtMillis = nextRevisionAtMillis
    )
}