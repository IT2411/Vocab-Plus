package com.vocabplus.app.data.repository

import com.vocabplus.app.core.util.DefaultDispatcherProvider
import com.vocabplus.app.core.util.Result
import com.vocabplus.app.data.datasource.AssetQuestionDataSource
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionRepositoryTest {

    private val mockDataSource = mockk<AssetQuestionDataSource>()
    private val repository = QuestionRepositoryImpl(
        dataSource = mockDataSource,
        dispatchers = DefaultDispatcherProvider()
    )

    private fun sampleList(category: Category, count: Int): List<Question> =
        (1..count).map {
            Question(
                id = "${category.slug}_$it",
                category = category,
                prompt = "Prompt $it",
                options = listOf("A", "B", "C", "D"),
                correctOptionIndex = 0,
                explanation = "Exp $it",
                exampleSentence = "Sentence $it"
            )
        }

    @Test
    fun `getQuestionsForDate is deterministic for same date and category`() = runTest {
        val synList = sampleList(Category.SYNONYM, 15)
        coEvery { mockDataSource.loadQuestionsForCategory(Category.SYNONYM) } returns synList
        coEvery { mockDataSource.loadQuestionsForCategory(Category.ANTONYM) } returns emptyList()
        coEvery { mockDataSource.loadQuestionsForCategory(Category.IDIOM) } returns emptyList()

        val result1 = repository.getQuestionsForDate("2026-09-02", Category.SYNONYM, 10)
        val result2 = repository.getQuestionsForDate("2026-09-02", Category.SYNONYM, 10)

        assertTrue(result1 is Result.Success)
        assertTrue(result2 is Result.Success)

        val list1 = (result1 as Result.Success).data
        val list2 = (result2 as Result.Success).data

        assertEquals(10, list1.size)
        assertEquals(list1.map { it.id }, list2.map { it.id })
    }
}