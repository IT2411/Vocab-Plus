package com.vocabplus.app.presentation.home

import com.vocabplus.app.domain.model.SectionState

data class HomeUiState(
    val totalPoints: Long = 0L,
    val gigaStreak: Int = 0,
    val synonymStreak: Int = 0,
    val antonymStreak: Int = 0,
    val idiomStreak: Int = 0,
    val synonymScore: Int = 0,
    val antonymScore: Int = 0,
    val idiomScore: Int = 0,
    val synonymState: SectionState = SectionState.NOT_STARTED,
    val antonymState: SectionState = SectionState.NOT_STARTED,
    val idiomState: SectionState = SectionState.NOT_STARTED,
    val revisionQuestionsCount: Int = 0,
    val isDailyCompleted: Boolean = false
)