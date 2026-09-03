package com.vocabplus.app.data.repository

import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.core.util.Result
import com.vocabplus.app.data.local.VocabDatabase
import com.vocabplus.app.data.local.entity.DailyAnswerEntity
import com.vocabplus.app.data.local.entity.QuestionHistoryEntity
import com.vocabplus.app.data.local.entity.UserStatsEntity
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionAnswer
import com.vocabplus.app.domain.model.QuestionHistory
import com.vocabplus.app.domain.model.UserStats
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomUserProgressRepository(
    private val database: VocabDatabase,
    private val questionRepository: QuestionRepository,
    private val dispatchers: DispatcherProvider
) : UserProgressRepository {

    private val userStatsDao = database.userStatsDao()
    private val dailyAnswerDao = database.dailyAnswerDao()
    private val questionHistoryDao = database.questionHistoryDao()

    override fun getUserStats(): Flow<UserStats> {
        return userStatsDao.getUserStatsFlow().map { entity ->
            entity?.toDomain() ?: UserStats()
        }
    }

    private suspend fun getOrCreateStats(): UserStatsEntity {
        val existing = userStatsDao.getUserStats()
        if (existing != null) return existing

        val initial = UserStatsEntity(id = 1)
        userStatsDao.insertOrUpdate(initial)
        return initial
    }

    override suspend fun addPoints(points: Int) = withContext(dispatchers.io) {
        if (points <= 0) return@withContext
        getOrCreateStats()
        userStatsDao.addPoints(points)
    }

    override suspend fun recordSectionCompleted(category: Category, isPerfect: Boolean) = withContext(dispatchers.io) {
        val stats = getOrCreateStats()
        val updated = when (category) {
            Category.SYNONYM -> {
                val newCurrent = if (isPerfect) stats.synonymCurrentStreak + 1 else 0
                val newBest = maxOf(newCurrent, stats.synonymBestStreak)
                stats.copy(
                    synonymCurrentStreak = newCurrent,
                    synonymBestStreak = newBest,
                    perfectSectionsCount = if (isPerfect) stats.perfectSectionsCount + 1 else stats.perfectSectionsCount
                )
            }
            Category.ANTONYM -> {
                val newCurrent = if (isPerfect) stats.antonymCurrentStreak + 1 else 0
                val newBest = maxOf(newCurrent, stats.antonymBestStreak)
                stats.copy(
                    antonymCurrentStreak = newCurrent,
                    antonymBestStreak = newBest,
                    perfectSectionsCount = if (isPerfect) stats.perfectSectionsCount + 1 else stats.perfectSectionsCount
                )
            }
            Category.IDIOM -> {
                val newCurrent = if (isPerfect) stats.idiomCurrentStreak + 1 else 0
                val newBest = maxOf(newCurrent, stats.idiomBestStreak)
                stats.copy(
                    idiomCurrentStreak = newCurrent,
                    idiomBestStreak = newBest,
                    perfectSectionsCount = if (isPerfect) stats.perfectSectionsCount + 1 else stats.perfectSectionsCount
                )
            }
        }
        userStatsDao.insertOrUpdate(updated)
    }

    override suspend fun recordGigaStreak(isPerfectDay: Boolean) = withContext(dispatchers.io) {
        val stats = getOrCreateStats()
        val newCurrent = if (isPerfectDay) stats.gigaCurrentStreak + 1 else 0
        val newBest = maxOf(newCurrent, stats.gigaBestStreak)
        val updated = stats.copy(
            gigaCurrentStreak = newCurrent,
            gigaBestStreak = newBest,
            perfectDaysCount = if (isPerfectDay) stats.perfectDaysCount + 1 else stats.perfectDaysCount
        )
        userStatsDao.insertOrUpdate(updated)
    }

    override suspend fun recordQuestionAnswer(
        dateIso: String,
        category: Category,
        answer: QuestionAnswer
    ) = withContext(dispatchers.io) {
        // Idempotent insertion into daily_answers (PRD §30)
        val answerEntity = DailyAnswerEntity(
            dateIso = dateIso,
            questionId = answer.questionId,
            categorySlug = category.slug,
            selectedOptionIndex = answer.selectedOptionIndex,
            isCorrect = answer.isCorrect,
            pointsAwarded = answer.pointsAwarded,
            answeredAtMillis = answer.answeredAtMillis
        )
        val insertedRowId = dailyAnswerDao.insertAnswer(answerEntity)

        // Only update lifetime aggregates if this answer was inserted for the first time
        if (insertedRowId != -1L) {
            val stats = getOrCreateStats()
            val updatedStats = stats.copy(
                totalQuestionsAnswered = stats.totalQuestionsAnswered + 1,
                totalCorrectAnswers = if (answer.isCorrect) stats.totalCorrectAnswers + 1 else stats.totalCorrectAnswers
            )
            userStatsDao.insertOrUpdate(updatedStats)

            // Update spaced repetition history
            val existing = questionHistoryDao.getHistory(answer.questionId)
            val now = answer.answeredAtMillis
            val threeDaysMillis = 3L * 24 * 60 * 60 * 1000

            val historyEntity = if (existing == null) {
                QuestionHistoryEntity(
                    questionId = answer.questionId,
                    firstSeenAtMillis = now,
                    lastSeenAtMillis = now,
                    timesSeen = 1,
                    timesCorrect = if (answer.isCorrect) 1 else 0,
                    timesIncorrect = if (answer.isCorrect) 0 else 1,
                    lastResultCorrect = answer.isCorrect,
                    nextRevisionAtMillis = now + threeDaysMillis
                )
            } else {
                existing.copy(
                    lastSeenAtMillis = now,
                    timesSeen = existing.timesSeen + 1,
                    timesCorrect = if (answer.isCorrect) existing.timesCorrect + 1 else existing.timesCorrect,
                    timesIncorrect = if (answer.isCorrect) existing.timesIncorrect else existing.timesIncorrect + 1,
                    lastResultCorrect = answer.isCorrect,
                    nextRevisionAtMillis = now + threeDaysMillis
                )
            }
            questionHistoryDao.insertOrUpdate(historyEntity)
        }
    }

    override suspend fun getQuestionHistory(questionId: String): QuestionHistory? = withContext(dispatchers.io) {
        questionHistoryDao.getHistory(questionId)?.toDomain()
    }

    override suspend fun getRevisionQuestions(limit: Int): List<Question> = withContext(dispatchers.io) {
        val eligibleEntities = questionHistoryDao.getEligibleForRevision(System.currentTimeMillis())
        val eligibleIds = if (eligibleEntities.isNotEmpty()) {
            eligibleEntities.map { it.questionId }
        } else {
            // Fallback to all seen questions if none reached the 3-day threshold yet
            questionHistoryDao.getAllHistory().map { it.questionId }
        }

        if (eligibleIds.isEmpty()) return@withContext emptyList()

        val allQuestionsResult = questionRepository.getAllQuestions()
        if (allQuestionsResult is Result.Success) {
            allQuestionsResult.data.filter { it.id in eligibleIds }.shuffled().take(limit)
        } else {
            emptyList()
        }
    }
}