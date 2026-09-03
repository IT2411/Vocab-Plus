package com.vocabplus.app.data.repository

import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.core.util.Result
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.DailyQuizState
import com.vocabplus.app.domain.model.SectionProgress
import com.vocabplus.app.domain.model.SectionState
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class QuizRepositoryImpl(
    private val questionRepository: QuestionRepository,
    private val dispatchers: DispatcherProvider
) : QuizRepository {

    private val mutex = Mutex()
    private val dailyQuizzes = mutableMapOf<String, MutableStateFlow<DailyQuizState>>()

    override fun getDailyQuizState(dateIso: String): Flow<DailyQuizState> {
        return getOrCreateFlow(dateIso).asStateFlow()
    }

    private fun getOrCreateFlow(dateIso: String): MutableStateFlow<DailyQuizState> {
        return dailyQuizzes.getOrPut(dateIso) {
            MutableStateFlow(
                DailyQuizState(
                    dateIso = dateIso,
                    synonyms = SectionProgress(Category.SYNONYM),
                    antonyms = SectionProgress(Category.ANTONYM),
                    idioms = SectionProgress(Category.IDIOM)
                )
            )
        }
    }

    override suspend fun getOrCreateDailyQuiz(dateIso: String): DailyQuizState = withContext(dispatchers.io) {
        mutex.withLock {
            val flow = getOrCreateFlow(dateIso)
            val current = flow.value

            if (current.synonyms.questions.isEmpty()) {
                val synResult = questionRepository.getQuestionsForDate(dateIso, Category.SYNONYM, 10)
                val antResult = questionRepository.getQuestionsForDate(dateIso, Category.ANTONYM, 10)
                val idmResult = questionRepository.getQuestionsForDate(dateIso, Category.IDIOM, 10)

                val synList = (synResult as? Result.Success)?.data.orEmpty()
                val antList = (antResult as? Result.Success)?.data.orEmpty()
                val idmList = (idmResult as? Result.Success)?.data.orEmpty()

                val populated = DailyQuizState(
                    dateIso = dateIso,
                    synonyms = SectionProgress(Category.SYNONYM, SectionState.NOT_STARTED, synList),
                    antonyms = SectionProgress(Category.ANTONYM, SectionState.NOT_STARTED, antList),
                    idioms = SectionProgress(Category.IDIOM, SectionState.NOT_STARTED, idmList)
                )
                flow.value = populated
                populated
            } else {
                current
            }
        }
    }

    override suspend fun getSectionProgress(dateIso: String, category: Category): SectionProgress = withContext(dispatchers.io) {
        val daily = getOrCreateDailyQuiz(dateIso)
        daily.getSection(category)
    }

    suspend fun updateSectionProgress(dateIso: String, progress: SectionProgress) = withContext(dispatchers.io) {
        mutex.withLock {
            val flow = getOrCreateFlow(dateIso)
            val current = flow.value
            val updated = when (progress.category) {
                Category.SYNONYM -> current.copy(synonyms = progress)
                Category.ANTONYM -> current.copy(antonyms = progress)
                Category.IDIOM -> current.copy(idioms = progress)
            }
            flow.value = updated
        }
    }
}