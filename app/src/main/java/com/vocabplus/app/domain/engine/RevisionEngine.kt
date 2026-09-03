package com.vocabplus.app.domain.engine

import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.QuestionHistory
import java.util.Random

object RevisionEngine {

    /**
     * Prioritizes and selects up to [limit] questions according to PRD §20:
     * - 40% Previously incorrect
     * - 30% Due for review (nextRevisionAt <= now)
     * - 20% Low historical accuracy (< 60%)
     * - 10% Random previously seen questions
     */
    fun selectRevisionQuestions(
        allQuestions: List<Question>,
        histories: List<QuestionHistory>,
        limit: Int = 10,
        currentTimeMillis: Long = System.currentTimeMillis(),
        random: Random = Random()
    ): List<Question> {
        if (histories.isEmpty() || allQuestions.isEmpty()) return emptyList()

        val questionMap = allQuestions.associateBy { it.id }
        val seenHistories = histories.filter { it.questionId in questionMap }
        if (seenHistories.isEmpty()) return emptyList()

        // 1. Previously incorrect (last answer was wrong)
        val incorrect = seenHistories.filter { !it.lastResultCorrect }

        // 2. Due for review
        val dueForReview = seenHistories.filter { it.nextRevisionAtMillis <= currentTimeMillis }

        // 3. Low accuracy (< 60%)
        val lowAccuracy = seenHistories.filter { it.accuracy < 60f }

        // 4. All seen
        val allSeen = seenHistories

        val selectedIds = mutableSetOf<String>()

        fun pickFromList(source: List<QuestionHistory>, count: Int) {
            val shuffled = source.map { it.questionId }.filter { it !in selectedIds }.shuffled(random)
            selectedIds.addAll(shuffled.take(count))
        }

        val targetIncorrect = (limit * 0.40).toInt().coerceAtLeast(1)
        val targetDue = (limit * 0.30).toInt().coerceAtLeast(1)
        val targetLowAccuracy = (limit * 0.20).toInt().coerceAtLeast(1)

        pickFromList(incorrect, targetIncorrect)
        pickFromList(dueForReview, targetDue)
        pickFromList(lowAccuracy, targetLowAccuracy)

        // Fill remainder with random seen questions
        val remaining = limit - selectedIds.size
        if (remaining > 0) {
            pickFromList(allSeen, remaining)
        }

        // Return mixed questions randomized across all 3 categories (PRD §18)
        return selectedIds
            .mapNotNull { questionMap[it] }
            .shuffled(random)
    }
}