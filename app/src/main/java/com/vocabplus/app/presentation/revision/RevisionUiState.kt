package com.vocabplus.app.presentation.revision

import com.vocabplus.app.domain.model.Question

sealed interface RevisionUiState {
    data object Loading : RevisionUiState

    data object Empty : RevisionUiState

    data class Active(
        val currentIndex: Int,
        val totalQuestions: Int,
        val currentQuestion: Question,
        val selectedOptionIndex: Int? = null,
        val isAnswerEvaluated: Boolean = false,
        val scoreSoFar: Int = 0,
        val bonusPointsEarned: Int = 0
    ) : RevisionUiState

    data class Completed(
        val score: Int,
        val totalQuestions: Int,
        val bonusPointsEarned: Int
    ) : RevisionUiState

    data class Error(val message: String) : RevisionUiState
}