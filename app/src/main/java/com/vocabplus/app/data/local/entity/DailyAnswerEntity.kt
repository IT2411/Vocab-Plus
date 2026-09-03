package com.vocabplus.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vocabplus.app.domain.model.QuestionAnswer

@Entity(
    tableName = "daily_answers",
    indices = [
        Index(value = ["date_iso", "question_id"], unique = true) // Idempotency constraint (PRD §30)
    ]
)
data class DailyAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date_iso") val dateIso: String,
    @ColumnInfo(name = "question_id") val questionId: String,
    @ColumnInfo(name = "category_slug") val categorySlug: String,
    @ColumnInfo(name = "selected_option_index") val selectedOptionIndex: Int,
    @ColumnInfo(name = "is_correct") val isCorrect: Boolean,
    @ColumnInfo(name = "points_awarded") val pointsAwarded: Int,
    @ColumnInfo(name = "answered_at_millis") val answeredAtMillis: Long
) {
    fun toDomain(): QuestionAnswer = QuestionAnswer(
        questionId = questionId,
        selectedOptionIndex = selectedOptionIndex,
        isCorrect = isCorrect,
        pointsAwarded = pointsAwarded,
        answeredAtMillis = answeredAtMillis
    )
}