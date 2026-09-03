package com.vocabplus.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vocabplus.app.core.util.DateUtils
import com.vocabplus.app.domain.repository.QuizRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val quizRepository: QuizRepository,
    private val userProgressRepository: UserProgressRepository,
    private val dateIso: String = DateUtils.todayIso()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            // Ensure today's quiz is initialized
            quizRepository.getOrCreateDailyQuiz(dateIso)

            combine(
                userProgressRepository.getUserStats(),
                quizRepository.getDailyQuizState(dateIso)
            ) { stats, dailyState ->
                HomeUiState(
                    totalPoints = stats.totalPoints,
                    gigaStreak = stats.gigaCurrentStreak,
                    synonymStreak = stats.synonymCurrentStreak,
                    antonymStreak = stats.antonymCurrentStreak,
                    idiomStreak = stats.idiomCurrentStreak,
                    synonymState = dailyState.synonyms.state,
                    antonymState = dailyState.antonyms.state,
                    idiomState = dailyState.idioms.state,
                    revisionQuestionsCount = userProgressRepository.getRevisionQuestions().size,
                    isDailyCompleted = dailyState.isAllCompleted
                )
            }.collect { newState ->
                _uiState.value = newState
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
                return HomeViewModel(quizRepository, userProgressRepository, dateIso) as T
            }
        }
    }
}