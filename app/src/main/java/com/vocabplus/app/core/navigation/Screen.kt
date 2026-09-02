package com.vocabplus.app.core.navigation

import com.vocabplus.app.domain.model.Category

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    
    data object Quiz : Screen("quiz/{categorySlug}") {
        fun createRoute(category: Category): String = "quiz/${category.slug}"
    }

    data object SectionResult : Screen("result/section/{categorySlug}/{score}/{total}") {
        fun createRoute(category: Category, score: Int, total: Int): String =
            "result/section/${category.slug}/$score/$total"
    }

    data object DailySummary : Screen("result/daily")
    data object Revision : Screen("revision")
    data object Statistics : Screen("statistics")
    data object Settings : Screen("settings")
}