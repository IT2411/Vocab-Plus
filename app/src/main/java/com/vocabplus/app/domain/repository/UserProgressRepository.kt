package com.vocabplus.app.domain.repository

import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionAnswer
import com.vocabplus.app.domain.model.QuestionHistory
import com.vocabplus.app.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserProgressRepository {
    fun getUserStats(): Flow<UserStats>
    suspend fun addPoints(points: Int)
    suspend fun recordSectionCompleted(category: Category, isPerfect: Boolean)
    suspend fun recordGigaStreak(isPerfectDay: Boolean)
    suspend fun recordQuestionAnswer(dateIso: String, category: Category, answer: QuestionAnswer)
    suspend fun getQuestionHistory(questionId: String): QuestionHistory?
    suspend fun getRevisionQuestions(limit: Int = 20): List<Question>
}