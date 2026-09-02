package com.vocabplus.app.domain.repository

import com.vocabplus.app.core.util.Result
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question

interface QuestionRepository {
    suspend fun getQuestionsByCategory(category: Category): Result<List<Question>>
    suspend fun getQuestionById(id: String): Result<Question>
    suspend fun getAllQuestions(): Result<List<Question>>
    suspend fun getQuestionsForDate(dateIso: String, category: Category, count: Int = 10): Result<List<Question>>
}