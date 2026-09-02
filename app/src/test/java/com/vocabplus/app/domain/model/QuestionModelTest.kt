package com.vocabplus.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class QuestionModelTest {

    @Test
    fun `valid question instantiation succeeds`() {
        val question = Question(
            id = "syn_001",
            category = Category.SYNONYM,
            prompt = "Which word most nearly means 'perfunctory'?",
            options = listOf("Thorough", "Superficial", "Enthusiastic", "Elaborate"),
            correctOptionIndex = 1,
            explanation = "Done with minimal effort.",
            exampleSentence = "He gave the report a perfunctory review."
        )

        assertEquals("Superficial", question.correctAnswer)
        assertEquals(Category.SYNONYM, question.category)
    }

    @Test
    fun `question with invalid option count throws exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "syn_002",
                category = Category.SYNONYM,
                prompt = "Prompt",
                options = listOf("Option 1", "Option 2"), // Only 2 options
                correctOptionIndex = 0,
                explanation = "Explanation",
                exampleSentence = "Sentence"
            )
        }
    }

    @Test
    fun `question with out of bounds correctOptionIndex throws exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            Question(
                id = "syn_003",
                category = Category.SYNONYM,
                prompt = "Prompt",
                options = listOf("A", "B", "C", "D"),
                correctOptionIndex = 4, // Out of bounds (0..3 valid)
                explanation = "Explanation",
                exampleSentence = "Sentence"
            )
        }
    }
}