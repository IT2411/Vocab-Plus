package com.vocabplus.app.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vocabplus.app.core.util.DateUtils
import com.vocabplus.app.domain.model.DailyQuizState
import com.vocabplus.app.domain.model.UserStats
import com.vocabplus.app.domain.repository.QuizRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class DailySummaryUiState(
    val totalScore: Int = 0,
    val totalQuestions: Int = 30,
    val totalPointsEarned: Int = 0,
    val synonymScore: Int = 0,
    val antonymScore: Int = 0,
    val idiomScore: Int = 0,
    val isGigaStreakAchieved: Boolean = false,
    val gigaStreakCount: Int = 0,
    val totalPoints: Long = 0L,
    val isLoading: Boolean = true
)

class DailySummaryViewModel(
    private val quizRepository: QuizRepository,
    private val userProgressRepository: UserProgressRepository,
    private val dateIso: String = DateUtils.todayIso()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailySummaryUiState())
    val uiState: StateFlow<DailySummaryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                quizRepository.getDailyQuizState(dateIso),
                userProgressRepository.getUserStats()
            ) { dailyState: DailyQuizState, stats: UserStats ->
                DailySummaryUiState(
                    totalScore = dailyState.totalDayScore,
                    totalQuestions = dailyState.synonyms.questions.size + dailyState.antonyms.questions.size + dailyState.idioms.questions.size,
                    totalPointsEarned = dailyState.totalDayPoints,
                    synonymScore = dailyState.synonyms.score,
                    antonymScore = dailyState.antonyms.score,
                    idiomScore = dailyState.idioms.score,
                    isGigaStreakAchieved = dailyState.isGigaStreakAchieved,
                    gigaStreakCount = stats.gigaCurrentStreak,
                    totalPoints = stats.totalPoints,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    companion object {
        fun provideFactory(
            quizRepository: QuizRepository,
            userProgressRepository: UserProgressRepository,
            dateIso: String = DateUtils.todayIso()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DailySummaryViewModel(quizRepository, userProgressRepository, dateIso) as T
            }
        }
    }
}