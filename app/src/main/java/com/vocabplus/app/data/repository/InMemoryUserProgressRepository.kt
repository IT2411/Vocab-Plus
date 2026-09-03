package com.vocabplus.app.data.repository

import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.core.util.Result
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionAnswer
import com.vocabplus.app.domain.model.QuestionHistory
import com.vocabplus.app.domain.model.UserStats
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class InMemoryUserProgressRepository(
    private val questionRepository: QuestionRepository,
    private val dispatchers: DispatcherProvider
) : UserProgressRepository {

    private val mutex = Mutex()
    private val statsFlow = MutableStateFlow(UserStats())
    private val questionHistories = mutableMapOf<String, QuestionHistory>()

    override fun getUserStats(): Flow<UserStats> = statsFlow.asStateFlow()

    override suspend fun addPoints(points: Int) = withContext(dispatchers.io) {
        if (points <= 0) return@withContext
        mutex.withLock {
            statsFlow.update { current ->
                current.copy(totalPoints = current.totalPoints + points)
            }
        }
    }

    override suspend fun recordSectionCompleted(category: Category, isPerfect: Boolean) = withContext(dispatchers.io) {
        mutex.withLock {
            statsFlow.update { current ->
                when (category) {
                    Category.SYNONYM -> {
                        val newCurrent = if (isPerfect) current.synonymCurrentStreak + 1 else 0
                        val newBest = maxOf(newCurrent, current.synonymBestStreak)
                        current.copy(
                            synonymCurrentStreak = newCurrent,
                            synonymBestStreak = newBest,
                            perfectSectionsCount = if (isPerfect) current.perfectSectionsCount + 1 else current.perfectSectionsCount
                        )
                    }
                    Category.ANTONYM -> {
                        val newCurrent = if (isPerfect) current.antonymCurrentStreak + 1 else 0
                        val newBest = maxOf(newCurrent, current.antonymBestStreak)
                        current.copy(
                            antonymCurrentStreak = newCurrent,
                            antonymBestStreak = newBest,
                            perfectSectionsCount = if (isPerfect) current.perfectSectionsCount + 1 else current.perfectSectionsCount
                        )
                    }
                    Category.IDIOM -> {
                        val newCurrent = if (isPerfect) current.idiomCurrentStreak + 1 else 0
                        val newBest = maxOf(newCurrent, current.idiomBestStreak)
                        current.copy(
                            idiomCurrentStreak = newCurrent,
                            idiomBestStreak = newBest,
                            perfectSectionsCount = if (isPerfect) current.perfectSectionsCount + 1 else current.perfectSectionsCount
                        )
                    }
                }
            }
        }
    }

    override suspend fun recordGigaStreak(isPerfectDay: Boolean) = withContext(dispatchers.io) {
        mutex.withLock {
            statsFlow.update { current ->
                val newCurrent = if (isPerfectDay) current.gigaCurrentStreak + 1 else 0
                val newBest = maxOf(newCurrent, current.gigaBestStreak)
                current.copy(
                    gigaCurrentStreak = newCurrent,
                    gigaBestStreak = newBest,
                    perfectDaysCount = if (isPerfectDay) current.perfectDaysCount + 1 else current.perfectDaysCount
                )
            }
        }
    }

    override suspend fun recordQuestionAnswer(
        dateIso: String,
        category: Category,
        answer: QuestionAnswer
    ) = withContext(dispatchers.io) {
        mutex.withLock {
            statsFlow.update { current ->
                current.copy(
                    totalQuestionsAnswered = current.totalQuestionsAnswered + 1,
                    totalCorrectAnswers = if (answer.isCorrect) current.totalCorrectAnswers + 1 else current.totalCorrectAnswers
                )
            }

            val existing = questionHistories[answer.questionId]
            val now = System.currentTimeMillis()
            val updated = if (existing == null) {
                QuestionHistory(
                    questionId = answer.questionId,
                    firstSeenAtMillis = now,
                    lastSeenAtMillis = now,
                    timesSeen = 1,
                    timesCorrect = if (answer.isCorrect) 1 else 0,
                    timesIncorrect = if (answer.isCorrect) 0 else 1,
                    lastResultCorrect = answer.isCorrect,
                    nextRevisionAtMillis = now + (3L * 24 * 60 * 60 * 1000) // 3 days delay
                )
            } else {
                existing.copy(
                    lastSeenAtMillis = now,
                    timesSeen = existing.timesSeen + 1,
                    timesCorrect = if (answer.isCorrect) existing.timesCorrect + 1 else existing.timesCorrect,
                    timesIncorrect = if (answer.isCorrect) existing.timesIncorrect else existing.timesIncorrect + 1,
                    lastResultCorrect = answer.isCorrect,
                    nextRevisionAtMillis = now + (3L * 24 * 60 * 60 * 1000)
                )
            }
            questionHistories[answer.questionId] = updated
        }
    }

    override suspend fun getQuestionHistory(questionId: String): QuestionHistory? = withContext(dispatchers.io) {
        mutex.withLock { questionHistories[questionId] }
    }

    override suspend fun getRevisionQuestions(limit: Int): List<Question> = withContext(dispatchers.io) {
        val seenIds = mutex.withLock { questionHistories.keys.toList() }
        val allQuestionsResult = questionRepository.getAllQuestions()
        if (allQuestionsResult is Result.Success) {
            allQuestionsResult.data.filter { it.id in seenIds }.shuffled().take(limit)
        } else {
            emptyList()
        }
    }
}