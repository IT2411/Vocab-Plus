package com.vocabplus.app.data.validator

import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentValidatorTest {

    @Test
    fun `valid questions pass validation with no errors`() {
        val questions = listOf(
            Question(
                id = "syn_01",
                category = Category.SYNONYM,
                prompt = "Prompt 1",
                options = listOf("A", "B", "C", "D"),
                correctOptionIndex = 0,
                explanation = "Explanation 1",
                exampleSentence = "Sentence 1"
            ),
            Question(
                id = "syn_02",
                category = Category.SYNONYM,
                prompt = "Prompt 2",
                options = listOf("W", "X", "Y", "Z"),
                correctOptionIndex = 2,
                explanation = "Explanation 2",
                exampleSentence = "Sentence 2"
            )
        )

        val result = ContentValidator.validateQuestions(questions)
        assertTrue(result is ContentValidator.ValidationResult.Valid)
    }

    @Test
    fun `duplicate question ID fails validation`() {
        val questions = listOf(
            Question("syn_01", Category.SYNONYM, "P1", listOf("A", "B", "C", "D"), 0, "Exp", "Ex"),
            Question("syn_01", Category.SYNONYM, "P2", listOf("W", "X", "Y", "Z"), 1, "Exp", "Ex")
        )

        val result = ContentValidator.validateQuestions(questions)
        assertTrue(result is ContentValidator.ValidationResult.Invalid)
        val errors = (result as ContentValidator.ValidationResult.Invalid).errors
        assertTrue(errors.any { it.contains("Duplicate question ID") })
    }
}