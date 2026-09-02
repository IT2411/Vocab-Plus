package com.vocabplus.app.domain.repository

import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.DailyQuizState
import com.vocabplus.app.domain.model.SectionProgress
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getDailyQuizState(dateIso: String): Flow<DailyQuizState>
    suspend fun getOrCreateDailyQuiz(dateIso: String): DailyQuizState
    suspend fun getSectionProgress(dateIso: String, category: Category): SectionProgress
}