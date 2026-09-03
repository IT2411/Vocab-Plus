package com.vocabplus.app.presentation.quiz

import com.vocabplus.app.core.util.Result
import com.vocabplus.app.core.util.TestDispatcherProvider
import com.vocabplus.app.data.repository.InMemoryUserProgressRepository
import com.vocabplus.app.data.repository.QuizRepositoryImpl
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.repository.QuestionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDispatchers = TestDispatcherProvider(testDispatcher)
    private val mockQuestionRepository = mockk<QuestionRepository>()

    private lateinit var userProgressRepository: InMemoryUserProgressRepository
    private lateinit var quizRepository: QuizRepositoryImpl

    private val testQuestions = (1..10).map { i ->
        Question(
            id = "syn_$i",
            category = Category.SYNONYM,
            prompt = "Question $i prompt",
            options = listOf("Option 0", "Option 1", "Option 2", "Option 3"),
            correctOptionIndex = 1,
            explanation = "Explanation $i",
            exampleSentence = "Sentence $i"
        )
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockQuestionRepository.getQuestionsForDate("2026-09-02", Category.SYNONYM, 10) } returns Result.Success(testQuestions)
        coEvery { mockQuestionRepository.getQuestionsForDate("2026-09-02", Category.ANTONYM, 10) } returns Result.Success(emptyList())
        coEvery { mockQuestionRepository.getQuestionsForDate("2026-09-02", Category.IDIOM, 10) } returns Result.Success(emptyList())
        coEvery { mockQuestionRepository.getAllQuestions() } returns Result.Success(testQuestions)

        userProgressRepository = InMemoryUserProgressRepository(mockQuestionRepository, testDispatchers)
        quizRepository = QuizRepositoryImpl(mockQuestionRepository, testDispatchers)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `quiz progresses through all 10 questions and finishes with perfect score`() = runTest(testDispatcher) {
        val viewModel = QuizViewModel(
            category = Category.SYNONYM,
            dateIso = "2026-09-02",
            quizRepository = quizRepository,
            userProgressRepository = userProgressRepository
        )

        // Verify initial active state
        val initialState = viewModel.uiState.value
        assertTrue(initialState is QuizUiState.Active)
        assertEquals(0, (initialState as QuizUiState.Active).currentIndex)

        // Answer questions 0 to 8
        for (q in 0..8) {
            viewModel.onOptionSelected(1) // Option 1 is correct
            val evaluated = viewModel.uiState.value as QuizUiState.Active
            assertTrue(evaluated.isAnswerEvaluated)
            assertEquals(q + 1, evaluated.scoreSoFar)

            viewModel.onContinue()
            val next = viewModel.uiState.value as QuizUiState.Active
            assertEquals(q + 1, next.currentIndex)
        }

        // Answer question 9 (10th question)
        viewModel.onOptionSelected(1)
        val lastEvaluated = viewModel.uiState.value as QuizUiState.Active
        assertTrue(lastEvaluated.isAnswerEvaluated)
        assertEquals(10, lastEvaluated.scoreSoFar)

        // Complete the quiz
        viewModel.onContinue()
        val finalState = viewModel.uiState.value
        assertTrue(finalState is QuizUiState.Completed)
        val completed = finalState as QuizUiState.Completed
        assertEquals(10, completed.score)
        assertEquals(10, completed.totalQuestions)
        assertEquals(100, completed.pointsEarned)
        assertTrue(completed.isStreakMaintained)
    }

    @Test
    fun `quiz with imperfect score resets streak`() = runTest(testDispatcher) {
        val viewModel = QuizViewModel(
            category = Category.SYNONYM,
            dateIso = "2026-09-02",
            quizRepository = quizRepository,
            userProgressRepository = userProgressRepository
        )

        // Answer first 9 correctly, last 1 wrong
        for (q in 0..8) {
            viewModel.onOptionSelected(1)
            viewModel.onContinue()
        }

        // Select incorrect option 0 on question 9
        viewModel.onOptionSelected(0)
        viewModel.onContinue()

        val finalState = viewModel.uiState.value
        assertTrue(finalState is QuizUiState.Completed)
        val completed = finalState as QuizUiState.Completed
        assertEquals(9, completed.score)
        assertEquals(90, completed.pointsEarned)
        assertTrue(!completed.isStreakMaintained)
    }
}