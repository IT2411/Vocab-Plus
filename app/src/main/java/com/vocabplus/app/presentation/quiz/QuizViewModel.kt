package com.vocabplus.app.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vocabplus.app.core.util.DateUtils
import com.vocabplus.app.data.repository.QuizRepositoryImpl
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionAnswer
import com.vocabplus.app.domain.model.SectionProgress
import com.vocabplus.app.domain.model.SectionState
import com.vocabplus.app.domain.repository.QuizRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val category: Category,
    private val dateIso: String = DateUtils.todayIso(),
    private val quizRepository: QuizRepository,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var questions: List<Question> = emptyList()
    private val answersMap = mutableMapOf<String, QuestionAnswer>()
    private var currentIndex = 0

    init {
        loadQuiz()
    }

    fun loadQuiz() {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            try {
                val dailyQuiz = quizRepository.getOrCreateDailyQuiz(dateIso)
                val sectionProgress = dailyQuiz.getSection(category)
                questions = sectionProgress.questions

                if (questions.isEmpty()) {
                    _uiState.value = QuizUiState.Error("No questions available for today.")
                    return@launch
                }

                // If already completed previously, show completed state
                if (sectionProgress.isComplete) {
                    val score = sectionProgress.score
                    val total = sectionProgress.questions.size
                    _uiState.value = QuizUiState.Completed(
                        category = category,
                        score = score,
                        totalQuestions = total,
                        pointsEarned = sectionProgress.totalPointsEarned,
                        isStreakMaintained = score == total
                    )
                    return@launch
                }

                // Restore previous answers if any
                answersMap.putAll(sectionProgress.answers)
                currentIndex = answersMap.size.coerceAtMost(questions.size - 1)

                showCurrentQuestion()
            } catch (e: Exception) {
                _uiState.value = QuizUiState.Error(e.localizedMessage ?: "Failed to load quiz")
            }
        }
    }

    private fun showCurrentQuestion() {
        if (currentIndex >= questions.size) {
            completeSection()
            return
        }

        val question = questions[currentIndex]
        val existingAnswer = answersMap[question.id]

        _uiState.value = QuizUiState.Active(
            category = category,
            currentIndex = currentIndex,
            totalQuestions = questions.size,
            currentQuestion = question,
            selectedOptionIndex = existingAnswer?.selectedOptionIndex,
            isAnswerEvaluated = existingAnswer != null,
            scoreSoFar = answersMap.values.count { it.isCorrect }
        )
    }

    fun onOptionSelected(selectedIndex: Int) {
        val currentState = _uiState.value as? QuizUiState.Active ?: return
        if (currentState.isAnswerEvaluated) return // Cannot re-answer (PRD §30)

        val question = currentState.currentQuestion
        val isCorrect = selectedIndex == question.correctOptionIndex
        val pointsAwarded = if (isCorrect) 10 else 0

        val answer = QuestionAnswer(
            questionId = question.id,
            selectedOptionIndex = selectedIndex,
            isCorrect = isCorrect,
            pointsAwarded = pointsAwarded,
            answeredAtMillis = System.currentTimeMillis()
        )

        answersMap[question.id] = answer

        viewModelScope.launch {
            // Persist points immediately (Idempotent per answer)
            userProgressRepository.recordQuestionAnswer(dateIso, category, answer)
            if (pointsAwarded > 0) {
                userProgressRepository.addPoints(pointsAwarded)
            }

            // Update in-progress state in repository
            if (quizRepository is QuizRepositoryImpl) {
                quizRepository.updateSectionProgress(
                    dateIso = dateIso,
                    progress = SectionProgress(
                        category = category,
                        state = SectionState.IN_PROGRESS,
                        questions = questions,
                        answers = answersMap.toMap()
                    )
                )
            }
        }

        _uiState.value = currentState.copy(
            selectedOptionIndex = selectedIndex,
            isAnswerEvaluated = true,
            scoreSoFar = answersMap.values.count { it.isCorrect }
        )
    }

    fun onContinue() {
        currentIndex++
        if (currentIndex >= questions.size) {
            completeSection()
        } else {
            showCurrentQuestion()
        }
    }

    private fun completeSection() {
        val score = answersMap.values.count { it.isCorrect }
        val total = questions.size
        val isPerfect = score == total
        val totalPoints = answersMap.values.sumOf { it.pointsAwarded }

        viewModelScope.launch {
            val progress = SectionProgress(
                category = category,
                state = SectionState.COMPLETED,
                questions = questions,
                answers = answersMap.toMap()
            )

            if (quizRepository is QuizRepositoryImpl) {
                quizRepository.updateSectionProgress(dateIso, progress)
            }

            userProgressRepository.recordSectionCompleted(category, isPerfect)

            // Check if day is all complete for Giga streak
            val dailyState = quizRepository.getOrCreateDailyQuiz(dateIso)
            if (dailyState.isAllCompleted) {
                userProgressRepository.recordGigaStreak(dailyState.isGigaStreakAchieved)
            }
        }

        _uiState.value = QuizUiState.Completed(
            category = category,
            score = score,
            totalQuestions = total,
            pointsEarned = totalPoints,
            isStreakMaintained = isPerfect
        )
    }

    companion object {
        fun provideFactory(
            category: Category,
            dateIso: String,
            quizRepository: QuizRepository,
            userProgressRepository: UserProgressRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return QuizViewModel(category, dateIso, quizRepository, userProgressRepository) as T
            }
        }
    }
}