package com.vocabplus.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryPerformanceTest {

    @Test
    fun `accuracy calculation handles zero questions answered safely`() {
        val perf = CategoryPerformance(category = Category.SYNONYM, questionsAnswered = 0, correctAnswers = 0)
        assertEquals(0f, perf.accuracy, 0.001f)
    }

    @Test
    fun `accuracy calculation computes correct percentage`() {
        val perf = CategoryPerformance(category = Category.IDIOM, questionsAnswered = 20, correctAnswers = 15)
        assertEquals(75.0f, perf.accuracy, 0.001f)
    }
}