package com.vocabplus.app.data.validator

import com.vocabplus.app.domain.model.Question

object ContentValidator {

    sealed interface ValidationResult {
        data object Valid : ValidationResult
        data class Invalid(val errors: List<String>) : ValidationResult
    }

    fun validateQuestions(questions: List<Question>): ValidationResult {
        val errors = mutableListOf<String>()
        val seenIds = mutableSetOf<String>()

        questions.forEachIndexed { index, question ->
            // Unique ID check
            if (!seenIds.add(question.id)) {
                errors.add("Duplicate question ID '${question.id}' at index $index")
            }

            // Options check
            if (question.options.size != 4) {
                errors.add("Question '${question.id}' must have exactly 4 options, found ${question.options.size}")
            }

            // Correct option index check
            if (question.correctOptionIndex !in 0..3) {
                errors.add("Question '${question.id}' has invalid correctOptionIndex '${question.correctOptionIndex}'")
            }

            // Empty text checks
            if (question.prompt.isBlank()) {
                errors.add("Question '${question.id}' has a blank prompt")
            }
            if (question.explanation.isBlank()) {
                errors.add("Question '${question.id}' has a blank explanation")
            }
            if (question.exampleSentence.isBlank()) {
                errors.add("Question '${question.id}' has a blank example sentence")
            }
            if (question.options.any { it.isBlank() }) {
                errors.add("Question '${question.id}' contains blank options")
            }

            // Unique options check (no identical distractors)
            if (question.options.distinct().size != 4) {
                errors.add("Question '${question.id}' contains duplicate options")
            }
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }
}