package com.vocabplus.app.domain.model

data class QuestionHistory(
    val questionId: String,
    val firstSeenAtMillis: Long,
    val lastSeenAtMillis: Long,
    val timesSeen: Int = 0,
    val timesCorrect: Int = 0,
    val timesIncorrect: Int = 0,
    val lastResultCorrect: Boolean = false,
    val nextRevisionAtMillis: Long = 0L
) {
    val accuracy: Float
        get() = if (timesSeen == 0) 0f else (timesCorrect.toFloat() / timesSeen) * 100f

    val isEligibleForRevision: Boolean
        get() = System.currentTimeMillis() >= nextRevisionAtMillis
}