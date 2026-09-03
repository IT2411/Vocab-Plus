package com.vocabplus.app.data.datasource

import android.content.Context
import com.vocabplus.app.data.model.QuestionDto
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface AssetQuestionDataSource {
    suspend fun loadQuestionsForCategory(category: Category): List<Question>
    suspend fun loadAllQuestions(): List<Question>
}

class AssetQuestionDataSourceImpl(
    private val context: Context,
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
) : AssetQuestionDataSource {

    override suspend fun loadQuestionsForCategory(category: Category): List<Question> =
        withContext(Dispatchers.IO) {
            val fileName = when (category) {
                Category.SYNONYM -> "questions/synonyms.json"
                Category.ANTONYM -> "questions/antonyms.json"
                Category.IDIOM -> "questions/idioms.json"
            }
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val dtos = json.decodeFromString<List<QuestionDto>>(jsonString)
            dtos.map { it.toDomain() }
        }

    override suspend fun loadAllQuestions(): List<Question> = withContext(Dispatchers.IO) {
        Category.entries.flatMap { category ->
            loadQuestionsForCategory(category)
        }
    }
}