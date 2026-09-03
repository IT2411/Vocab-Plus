package com.vocabplus.app.data.repository

import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.core.util.Result
import com.vocabplus.app.data.datasource.AssetQuestionDataSource
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.repository.QuestionRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Random

class QuestionRepositoryImpl(
    private val dataSource: AssetQuestionDataSource,
    private val dispatchers: DispatcherProvider
) : QuestionRepository {

    private val cache = mutableMapOf<Category, List<Question>>()
    private val mutex = Mutex()

    private suspend fun ensureLoaded(): Map<Category, List<Question>> = mutex.withLock {
        if (cache.isEmpty()) {
            for (category in Category.entries) {
                val questions = dataSource.loadQuestionsForCategory(category)
                cache[category] = questions
            }
        }
        cache
    }

    override suspend fun getQuestionsByCategory(category: Category): Result<List<Question>> =
        withContext(dispatchers.io) {
            try {
                val loadedCache = ensureLoaded()
                val questions = loadedCache[category].orEmpty()
                Result.Success(questions)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getQuestionById(id: String): Result<Question> =
        withContext(dispatchers.io) {
            try {
                val loadedCache = ensureLoaded()
                val question = loadedCache.values.flatten().find { it.id == id }
                    ?: return@withContext Result.Error(NoSuchElementException("Question with ID '$id' not found"))
                Result.Success(question)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override suspend fun getAllQuestions(): Result<List<Question>> =
        withContext(dispatchers.io) {
            try {
                val loadedCache = ensureLoaded()
                Result.Success(loadedCache.values.flatten())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    /**
     * Deterministic question selection based on calendar date ISO string (e.g. "2026-09-02").
     * Uses a pseudo-random permutation seeded by date hash + category hash (PRD §16).
     */
    override suspend fun getQuestionsForDate(
        dateIso: String,
        category: Category,
        count: Int
    ): Result<List<Question>> = withContext(dispatchers.io) {
        try {
            val loadedCache = ensureLoaded()
            val questions = loadedCache[category].orEmpty()
            if (questions.isEmpty()) {
                return@withContext Result.Success(emptyList())
            }

            val seed = (dateIso.hashCode().toLong() shl 16) xor category.slug.hashCode().toLong()
            val random = Random(seed)

            // Permute deterministically
            val shuffled = questions.toMutableList()
            for (i in shuffled.size - 1 downTo 1) {
                val j = random.nextInt(i + 1)
                val temp = shuffled[i]
                shuffled[i] = shuffled[j]
                shuffled[j] = temp
            }

            Result.Success(shuffled.take(count))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}