package com.vocabplus.app.domain.model

data class DailyQuizState(
    val dateIso: String,
    val synonyms: SectionProgress,
    val antonyms: SectionProgress,
    val idioms: SectionProgress
) {
    val isAllCompleted: Boolean
        get() = synonyms.isComplete && antonyms.isComplete && idioms.isComplete

    val isGigaStreakAchieved: Boolean
        get() = synonyms.isPerfect && antonyms.isPerfect && idioms.isPerfect

    val totalDayScore: Int
        get() = synonyms.score + antonyms.score + idioms.score

    val totalDayPoints: Int
        get() = synonyms.totalPointsEarned + antonyms.totalPointsEarned + idioms.totalPointsEarned

    fun getSection(category: Category): SectionProgress = when (category) {
        Category.SYNONYM -> synonyms
        Category.ANTONYM -> antonyms
        Category.IDIOM -> idioms
    }
}