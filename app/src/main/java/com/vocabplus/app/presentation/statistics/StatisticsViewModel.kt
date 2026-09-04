package com.vocabplus.app.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vocabplus.app.data.local.VocabDatabase
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.CategoryPerformance
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val database: VocabDatabase,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState(isLoading = true))
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            combine(
                userProgressRepository.getUserStats(),
                database.dailyAnswerDao().getCategoryStatsFlow()
            ) { stats, categoryTuples ->
                val tuplesMap = categoryTuples.associateBy { it.category_slug }

                val synTuple = tuplesMap[Category.SYNONYM.slug]
                val antTuple = tuplesMap[Category.ANTONYM.slug]
                val idmTuple = tuplesMap[Category.IDIOM.slug]

                val synPerf = CategoryPerformance(
                    category = Category.SYNONYM,
                    questionsAnswered = synTuple?.total_answered ?: 0,
                    correctAnswers = synTuple?.total_correct ?: 0,
                    currentStreak = stats.synonymCurrentStreak,
                    bestStreak = stats.synonymBestStreak
                )

                val antPerf = CategoryPerformance(
                    category = Category.ANTONYM,
                    questionsAnswered = antTuple?.total_answered ?: 0,
                    correctAnswers = antTuple?.total_correct ?: 0,
                    currentStreak = stats.antonymCurrentStreak,
                    bestStreak = stats.antonymBestStreak
                )

                val idmPerf = CategoryPerformance(
                    category = Category.IDIOM,
                    questionsAnswered = idmTuple?.total_answered ?: 0,
                    correctAnswers = idmTuple?.total_correct ?: 0,
                    currentStreak = stats.idiomCurrentStreak,
                    bestStreak = stats.idiomBestStreak
                )

                StatisticsUiState(
                    overallStats = stats,
                    synonymPerformance = synPerf,
                    antonymPerformance = antPerf,
                    idiomPerformance = idmPerf,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    companion object {
        fun provideFactory(
            database: VocabDatabase,
            userProgressRepository: UserProgressRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StatisticsViewModel(database, userProgressRepository) as T
            }
        }
    }
}