package com.vocabplus.app.presentation.quiz

import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question

sealed interface QuizUiState {
    data object Loading : QuizUiState

    data class Active(
        val category: Category,
        val currentIndex: Int,
        val totalQuestions: Int,
        val currentQuestion: Question,
        val selectedOptionIndex: Int? = null,
        val isAnswerEvaluated: Boolean = false,
        val scoreSoFar: Int = 0
    ) : QuizUiState

    data class Completed(
        val category: Category,
        val score: Int,
        val totalQuestions: Int,
        val pointsEarned: Int,
        val isStreakMaintained: Boolean
    ) : QuizUiState

    data class Error(val message: String) : QuizUiState
}