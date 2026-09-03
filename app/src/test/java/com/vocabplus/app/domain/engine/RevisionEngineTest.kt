package com.vocabplus.app.domain.engine

import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionHistory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random

class RevisionEngineTest {

    private fun dummyQuestion(id: String, category: Category): Question = Question(
        id = id,
        category = category,
        prompt = "Prompt $id",
        options = listOf("A", "B", "C", "D"),
        correctOptionIndex = 0,
        explanation = "Exp",
        exampleSentence = "Ex"
    )

    @Test
    fun `revision engine prioritizes incorrect and due questions`() {
        val questions = (1..20).map { i ->
            val cat = when {
                i % 3 == 0 -> Category.SYNONYM
                i % 3 == 1 -> Category.ANTONYM
                else -> Category.IDIOM
            }
            dummyQuestion("q_$i", cat)
        }

        val histories = questions.mapIndexed { idx, q ->
            QuestionHistory(
                questionId = q.id,
                firstSeenAtMillis = 1000L,
                lastSeenAtMillis = 1000L,
                timesSeen = 2,
                timesCorrect = if (idx < 5) 0 else 2, // First 5 are incorrect
                timesIncorrect = if (idx < 5) 2 else 0,
                lastResultCorrect = idx >= 5,
                nextRevisionAtMillis = if (idx in 5..8) 500L else 2000L // 5..8 are due
            )
        }

        val selected = RevisionEngine.selectRevisionQuestions(
            allQuestions = questions,
            histories = histories,
            limit = 8,
            currentTimeMillis = 1000L,
            random = Random(42)
        )

        assertEquals(8, selected.size)
        // Must contain previously incorrect questions
        val selectedIds = selected.map { it.id }.toSet()
        assertTrue(selectedIds.any { it in listOf("q_1", "q_2", "q_3", "q_4", "q_5") })
    }
}