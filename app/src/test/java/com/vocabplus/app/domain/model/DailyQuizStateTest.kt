package com.vocabplus.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyQuizStateTest {

    private fun createDummyQuestion(id: String, category: Category): Question = Question(
        id = id,
        category = category,
        prompt = "Prompt $id",
        options = listOf("A", "B", "C", "D"),
        correctOptionIndex = 0,
        explanation = "Exp",
        exampleSentence = "Ex"
    )

    @Test
    fun `giga streak is achieved only when all 3 sections are perfect`() {
        val synQuestions = (1..10).map { createDummyQuestion("syn_$it", Category.SYNONYM) }
        val antQuestions = (1..10).map { createDummyQuestion("ant_$it", Category.ANTONYM) }
        val idmQuestions = (1..10).map { createDummyQuestion("idm_$it", Category.IDIOM) }

        val synAnswers = synQuestions.associate {
            it.id to QuestionAnswer(it.id, 0, true, 10, 1000L)
        }
        val antAnswers = antQuestions.associate {
            it.id to QuestionAnswer(it.id, 0, true, 10, 1000L)
        }
        val idmAnswers = idmQuestions.associate {
            it.id to QuestionAnswer(it.id, 0, true, 10, 1000L)
        }

        val synProgress = SectionProgress(Category.SYNONYM, SectionState.COMPLETED, synQuestions, synAnswers)
        val antProgress = SectionProgress(Category.ANTONYM, SectionState.COMPLETED, antQuestions, antAnswers)
        val idmProgress = SectionProgress(Category.IDIOM, SectionState.COMPLETED, idmQuestions, idmAnswers)

        val perfectQuizState = DailyQuizState("2026-09-02", synProgress, antProgress, idmProgress)

        assertTrue(perfectQuizState.isAllCompleted)
        assertTrue(perfectQuizState.isGigaStreakAchieved)
        assertEquals(30, perfectQuizState.totalDayScore)
        assertEquals(300, perfectQuizState.totalDayPoints)

        // 9/10 in idioms breaks giga streak
        val imperfectIdmAnswers = idmAnswers.toMutableMap().apply {
            put("idm_10", QuestionAnswer("idm_10", 1, false, 0, 1000L))
        }
        val imperfectIdmProgress = SectionProgress(Category.IDIOM, SectionState.COMPLETED, idmQuestions, imperfectIdmAnswers)
        val imperfectQuizState = DailyQuizState("2026-09-02", synProgress, antProgress, imperfectIdmProgress)

        assertTrue(imperfectQuizState.isAllCompleted)
        assertFalse(imperfectQuizState.isGigaStreakAchieved)
        assertEquals(29, imperfectQuizState.totalDayScore)
        assertEquals(290, imperfectQuizState.totalDayPoints)
    }
}