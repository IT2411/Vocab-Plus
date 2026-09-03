package com.vocabplus.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vocabplus.app.domain.model.UserStats

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1, // Single-row aggregate
    @ColumnInfo(name = "total_points") val totalPoints: Long = 0L,
    @ColumnInfo(name = "synonym_current_streak") val synonymCurrentStreak: Int = 0,
    @ColumnInfo(name = "synonym_best_streak") val synonymBestStreak: Int = 0,
    @ColumnInfo(name = "antonym_current_streak") val antonymCurrentStreak: Int = 0,
    @ColumnInfo(name = "antonym_best_streak") val antonymBestStreak: Int = 0,
    @ColumnInfo(name = "idiom_current_streak") val idiomCurrentStreak: Int = 0,
    @ColumnInfo(name = "idiom_best_streak") val idiomBestStreak: Int = 0,
    @ColumnInfo(name = "giga_current_streak") val gigaCurrentStreak: Int = 0,
    @ColumnInfo(name = "giga_best_streak") val gigaBestStreak: Int = 0,
    @ColumnInfo(name = "total_questions_answered") val totalQuestionsAnswered: Int = 0,
    @ColumnInfo(name = "total_correct_answers") val totalCorrectAnswers: Int = 0,
    @ColumnInfo(name = "perfect_sections_count") val perfectSectionsCount: Int = 0,
    @ColumnInfo(name = "perfect_days_count") val perfectDaysCount: Int = 0
) {
    fun toDomain(): UserStats = UserStats(
        totalPoints = totalPoints,
        synonymCurrentStreak = synonymCurrentStreak,
        synonymBestStreak = synonymBestStreak,
        antonymCurrentStreak = antonymCurrentStreak,
        antonymBestStreak = antonymBestStreak,
        idiomCurrentStreak = idiomCurrentStreak,
        idiomBestStreak = idiomBestStreak,
        gigaCurrentStreak = gigaCurrentStreak,
        gigaBestStreak = gigaBestStreak,
        totalQuestionsAnswered = totalQuestionsAnswered,
        totalCorrectAnswers = totalCorrectAnswers,
        perfectSectionsCount = perfectSectionsCount,
        perfectDaysCount = perfectDaysCount
    )
}