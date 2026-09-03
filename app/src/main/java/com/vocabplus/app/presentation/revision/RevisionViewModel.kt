package com.vocabplus.app.presentation.revision

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vocabplus.app.core.util.DateUtils
import com.vocabplus.app.domain.engine.RevisionEngine
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionAnswer
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RevisionViewModel(
    private val questionRepository: QuestionRepository,
    private val userProgressRepository: UserProgressRepository,
    private val dateIso: String = DateUtils.todayIso()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RevisionUiState>(RevisionUiState.Loading)
    val uiState: StateFlow<RevisionUiState> = _uiState.asStateFlow()

    private var revisionQuestions: List<Question> = emptyList()
    private var currentIndex = 0
    private var correctCount = 0
    private var revisionBonusPoints = 0
    private val maxDailyBonusPoints = 20 // Daily revision point cap (PRD §21)

    init {
        loadRevision()
    }

    fun loadRevision() {
        viewModelScope.launch {
            _uiState.value = RevisionUiState.Loading
            try {
                val questions = userProgressRepository.getRevisionQuestions(limit = 10)
                if (questions.isEmpty()) {
                    _uiState.value = RevisionUiState.Empty
                    return@launch
                }

                revisionQuestions = questions
                currentIndex = 0
                correctCount = 0
                revisionBonusPoints = 0
                showCurrentQuestion()
            } catch (e: Exception) {
                _uiState.value = RevisionUiState.Error(e.localizedMessage ?: "Failed to load revision")
            }
        }
    }

    private fun showCurrentQuestion() {
        if (currentIndex >= revisionQuestions.size) {
            completeRevision()
            return
        }

        val question = revisionQuestions[currentIndex]
        _uiState.value = RevisionUiState.Active(
            currentIndex = currentIndex,
            totalQuestions = revisionQuestions.size,
            currentQuestion = question,
            selectedOptionIndex = null,
            isAnswerEvaluated = false,
            scoreSoFar = correctCount,
            bonusPointsEarned = revisionBonusPoints
        )
    }

    fun onOptionSelected(selectedIndex: Int) {
        val currentState = _uiState.value as? RevisionUiState.Active ?: return
        if (currentState.isAnswerEvaluated) return

        val question = currentState.currentQuestion
        val isCorrect = selectedIndex == question.correctOptionIndex

        // Revision rule: +2 points per correct answer, capped at 20 (PRD §21)
        val pointsToAward = if (isCorrect && revisionBonusPoints + 2 <= maxDailyBonusPoints) 2 else 0

        if (isCorrect) {
            correctCount++
            revisionBonusPoints += pointsToAward
        }

        viewModelScope.launch {
            val answer = QuestionAnswer(
                questionId = question.id,
                selectedOptionIndex = selectedIndex,
                isCorrect = isCorrect,
                pointsAwarded = pointsToAward,
                answeredAtMillis = System.currentTimeMillis()
            )

            userProgressRepository.recordQuestionAnswer(dateIso, question.category, answer)
            if (pointsToAward > 0) {
                userProgressRepository.addPoints(pointsToAward)
            }
        }

        _uiState.value = currentState.copy(
            selectedOptionIndex = selectedIndex,
            isAnswerEvaluated = true,
            scoreSoFar = correctCount,
            bonusPointsEarned = revisionBonusPoints
        )
    }

    fun onContinue() {
        currentIndex++
        if (currentIndex >= revisionQuestions.size) {
            completeRevision()
        } else {
            showCurrentQuestion()
        }
    }

    private fun completeRevision() {
        _uiState.value = RevisionUiState.Completed(
            score = correctCount,
            totalQuestions = revisionQuestions.size,
            bonusPointsEarned = revisionBonusPoints
        )
    }

    companion object {
        fun provideFactory(
            questionRepository: QuestionRepository,
            userProgressRepository: UserProgressRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RevisionViewModel(questionRepository, userProgressRepository) as T
            }
        }
    }
}