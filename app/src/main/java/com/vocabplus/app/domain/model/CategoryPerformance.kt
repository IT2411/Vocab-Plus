package com.vocabplus.app.domain.model

data class CategoryPerformance(
    val category: Category,
    val questionsAnswered: Int = 0,
    val correctAnswers: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
) {
    val accuracy: Float
        get() = if (questionsAnswered == 0) 0f else (correctAnswers.toFloat() / questionsAnswered) * 100f
}