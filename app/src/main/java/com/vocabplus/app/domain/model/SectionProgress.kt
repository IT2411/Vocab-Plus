package com.vocabplus.app.domain.model

enum class SectionState {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

data class QuestionAnswer(
    val questionId: String,
    val selectedOptionIndex: Int,
    val isCorrect: Boolean,
    val pointsAwarded: Int,
    val answeredAtMillis: Long
)

data class SectionProgress(
    val category: Category,
    val state: SectionState = SectionState.NOT_STARTED,
    val questions: List<Question> = emptyList(),
    val answers: Map<String, QuestionAnswer> = emptyMap()
) {
    val score: Int
        get() = answers.values.count { it.isCorrect }

    val totalPointsEarned: Int
        get() = answers.values.sumOf { it.pointsAwarded }

    val isPerfect: Boolean
        get() = state == SectionState.COMPLETED && questions.isNotEmpty() && score == questions.size

    val isComplete: Boolean
        get() = state == SectionState.COMPLETED
}