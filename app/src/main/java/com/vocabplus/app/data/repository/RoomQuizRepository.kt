package com.vocabplus.app.data.repository

import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.core.util.Result
import com.vocabplus.app.data.local.VocabDatabase
import com.vocabplus.app.data.local.entity.DailySectionEntity
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.DailyQuizState
import com.vocabplus.app.domain.model.SectionProgress
import com.vocabplus.app.domain.model.SectionState
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RoomQuizRepository(
    private val database: VocabDatabase,
    private val questionRepository: QuestionRepository,
    private val dispatchers: DispatcherProvider
) : QuizRepository {

    private val dailySectionDao = database.dailySectionDao()
    private val dailyAnswerDao = database.dailyAnswerDao()

    override fun getDailyQuizState(dateIso: String): Flow<DailyQuizState> {
        return combine(
            dailySectionDao.getSectionsForDateFlow(dateIso),
            dailyAnswerDao.getAnswersForSectionFlow(dateIso, Category.SYNONYM.slug),
            dailyAnswerDao.getAnswersForSectionFlow(dateIso, Category.ANTONYM.slug),
            dailyAnswerDao.getAnswersForSectionFlow(dateIso, Category.IDIOM.slug)
        ) { _, synAnswers, antAnswers, idmAnswers ->
            val synProgress = buildSectionProgress(dateIso, Category.SYNONYM, synAnswers.map { it.toDomain() })
            val antProgress = buildSectionProgress(dateIso, Category.ANTONYM, antAnswers.map { it.toDomain() })
            val idmProgress = buildSectionProgress(dateIso, Category.IDIOM, idmAnswers.map { it.toDomain() })

            DailyQuizState(
                dateIso = dateIso,
                synonyms = synProgress,
                antonyms = antProgress,
                idioms = idmProgress
            )
        }.flowOn(dispatchers.io)
    }

    private suspend fun buildSectionProgress(
        dateIso: String,
        category: Category,
        answers: List<com.vocabplus.app.domain.model.QuestionAnswer>
    ): SectionProgress {
        val qResult = questionRepository.getQuestionsForDate(dateIso, category, 10)
        val questions = (qResult as? Result.Success)?.data.orEmpty()
        val sectionEntity = dailySectionDao.getSection(dateIso, category.slug)

        val answersMap = answers.associateBy { it.questionId }
        val state = when {
            sectionEntity?.isCompleted == true -> SectionState.COMPLETED
            answersMap.isNotEmpty() -> SectionState.IN_PROGRESS
            else -> SectionState.NOT_STARTED
        }

        return SectionProgress(
            category = category,
            state = state,
            questions = questions,
            answers = answersMap
        )
    }

    override suspend fun getOrCreateDailyQuiz(dateIso: String): DailyQuizState = withContext(dispatchers.io) {
        val synAnswers = dailyAnswerDao.getAnswersForSection(dateIso, Category.SYNONYM.slug).map { it.toDomain() }
        val antAnswers = dailyAnswerDao.getAnswersForSection(dateIso, Category.ANTONYM.slug).map { it.toDomain() }
        val idmAnswers = dailyAnswerDao.getAnswersForSection(dateIso, Category.IDIOM.slug).map { it.toDomain() }

        val synProgress = buildSectionProgress(dateIso, Category.SYNONYM, synAnswers)
        val antProgress = buildSectionProgress(dateIso, Category.ANTONYM, antAnswers)
        val idmProgress = buildSectionProgress(dateIso, Category.IDIOM, idmAnswers)

        DailyQuizState(
            dateIso = dateIso,
            synonyms = synProgress,
            antonyms = antProgress,
            idioms = idmProgress
        )
    }

    override suspend fun getSectionProgress(dateIso: String, category: Category): SectionProgress = withContext(dispatchers.io) {
        val answers = dailyAnswerDao.getAnswersForSection(dateIso, category.slug).map { it.toDomain() }
        buildSectionProgress(dateIso, category, answers)
    }

    suspend fun updateSectionProgress(dateIso: String, progress: SectionProgress) = withContext(dispatchers.io) {
        val entity = DailySectionEntity(
            dateIso = dateIso,
            categorySlug = progress.category.slug,
            state = progress.state.name,
            score = progress.score,
            totalPoints = progress.totalPointsEarned,
            isCompleted = progress.isComplete
        )
        dailySectionDao.insertOrUpdate(entity)
    }
}