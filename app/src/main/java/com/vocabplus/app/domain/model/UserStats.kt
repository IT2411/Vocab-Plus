package com.vocabplus.app.domain.model

data class UserStats(
    val totalPoints: Long = 0L,
    val synonymCurrentStreak: Int = 0,
    val synonymBestStreak: Int = 0,
    val antonymCurrentStreak: Int = 0,
    val antonymBestStreak: Int = 0,
    val idiomCurrentStreak: Int = 0,
    val idiomBestStreak: Int = 0,
    val gigaCurrentStreak: Int = 0,
    val gigaBestStreak: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val perfectSectionsCount: Int = 0,
    val perfectDaysCount: Int = 0
) {
    val overallAccuracy: Float
        get() = if (totalQuestionsAnswered == 0) 0f else (totalCorrectAnswers.toFloat() / totalQuestionsAnswered) * 100f

    fun getStreakForCategory(category: Category): Int = when (category) {
        Category.SYNONYM -> synonymCurrentStreak
        Category.ANTONYM -> antonymCurrentStreak
        Category.IDIOM -> idiomCurrentStreak
    }

    fun getBestStreakForCategory(category: Category): Int = when (category) {
        Category.SYNONYM -> synonymBestStreak
        Category.ANTONYM -> antonymBestStreak
        Category.IDIOM -> idiomBestStreak
    }
}