package com.vocabplus.app.presentation.revision

import com.vocabplus.app.core.util.TestDispatcherProvider
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.UserProgressRepository
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
class RevisionViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockQuestionRepository = mockk<QuestionRepository>()
    private val mockUserProgressRepository = mockk<UserProgressRepository>(relaxed = true)

    private val sampleRevisionQuestions = listOf(
        Question("syn_1", Category.SYNONYM, "Prompt 1", listOf("A", "B", "C", "D"), 1, "Exp", "Ex"),
        Question("idm_1", Category.IDIOM, "Prompt 2", listOf("A", "B", "C", "D"), 2, "Exp", "Ex")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockUserProgressRepository.getRevisionQuestions(any()) } returns sampleRevisionQuestions
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `revision awards +2 bonus points per correct answer`() = runTest(testDispatcher) {
        val viewModel = RevisionViewModel(mockQuestionRepository, mockUserProgressRepository)

        val active1 = viewModel.uiState.value as RevisionUiState.Active
        assertEquals(0, active1.currentIndex)
        assertEquals("syn_1", active1.currentQuestion.id)

        // Select correct option 1 (+2 points)
        viewModel.onOptionSelected(1)
        val evaluated1 = viewModel.uiState.value as RevisionUiState.Active
        assertEquals(2, evaluated1.bonusPointsEarned)
        assertEquals(1, evaluated1.scoreSoFar)

        viewModel.onContinue()

        val active2 = viewModel.uiState.value as RevisionUiState.Active
        assertEquals(1, active2.currentIndex)
        assertEquals("idm_1", active2.currentQuestion.id)

        // Select correct option 2 (+2 points -> total 4)
        viewModel.onOptionSelected(2)
        viewModel.onContinue()

        val completed = viewModel.uiState.value as RevisionUiState.Completed
        assertEquals(2, completed.score)
        assertEquals(2, completed.totalQuestions)
        assertEquals(4, completed.bonusPointsEarned)
    }
}