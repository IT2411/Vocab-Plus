package com.vocabplus.app.data.model

import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: String,
    val category: String,
    val prompt: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val exampleSentence: String,
    val difficulty: Int = 3
) {
    fun toDomain(): Question {
        val domainCategory = Category.fromSlug(category)
            ?: throw IllegalArgumentException("Unknown category slug '$category' in question $id")

        return Question(
            id = id,
            category = domainCategory,
            prompt = prompt,
            options = options,
            correctOptionIndex = correctOptionIndex,
            explanation = explanation,
            exampleSentence = exampleSentence,
            difficulty = difficulty
        )
    }
}