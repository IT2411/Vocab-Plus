package com.vocabplus.app.presentation.statistics

import com.vocabplus.app.domain.model.CategoryPerformance
import com.vocabplus.app.domain.model.UserStats

data class StatisticsUiState(
    val overallStats: UserStats = UserStats(),
    val synonymPerformance: CategoryPerformance = CategoryPerformance(com.vocabplus.app.domain.model.Category.SYNONYM),
    val antonymPerformance: CategoryPerformance = CategoryPerformance(com.vocabplus.app.domain.model.Category.ANTONYM),
    val idiomPerformance: CategoryPerformance = CategoryPerformance(com.vocabplus.app.domain.model.Category.IDIOM),
    val isLoading: Boolean = false
)