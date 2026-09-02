package com.vocabplus.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val category: Category,
    val prompt: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val exampleSentence: String,
    val difficulty: Int = 3
) {
    init {
        require(id.isNotBlank()) { "Question ID cannot be blank" }
        require(prompt.isNotBlank()) { "Question prompt cannot be blank" }
        require(options.size == 4) { "Question must contain exactly 4 options. Found: ${options.size}" }
        require(correctOptionIndex in 0..3) { "Correct option index must be between 0 and 3. Found: $correctOptionIndex" }
        require(explanation.isNotBlank()) { "Question explanation cannot be blank" }
        require(exampleSentence.isNotBlank()) { "Question example sentence cannot be blank" }
    }

    val correctAnswer: String
        get() = options[correctOptionIndex]
}