package com.vocabplus.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.domain.model.SectionState
import com.vocabplus.app.domain.model.UserStats
import com.vocabplus.app.presentation.home.HomeScreen
import com.vocabplus.app.presentation.quiz.QuizScreen
import com.vocabplus.app.presentation.result.DailySummaryScreen
import com.vocabplus.app.presentation.result.SectionResultScreen
import com.vocabplus.app.presentation.revision.RevisionScreen
import com.vocabplus.app.presentation.settings.SettingsScreen
import com.vocabplus.app.presentation.statistics.StatisticsScreen

// Sample static questions for UI review
private val previewSampleQuestion = Question(
    id = "syn_preview",
    category = Category.SYNONYM,
    prompt = "Which word most nearly means 'perfunctory'?",
    options = listOf("Thorough", "Superficial", "Enthusiastic", "Elaborate"),
    correctOptionIndex = 1,
    explanation = "Perfunctory describes something done with minimal effort, care, or interest.",
    exampleSentence = "He gave the legal document a perfunctory review before signing."
)

@Composable
fun VocabNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                totalPoints = 12840L,
                gigaStreak = 7,
                synonymStreak = 12,
                antonymStreak = 9,
                idiomStreak = 4,
                synonymState = SectionState.NOT_STARTED,
                antonymState = SectionState.NOT_STARTED,
                idiomState = SectionState.NOT_STARTED,
                revisionQuestionsCount = 24,
                onCategoryClick = { category ->
                    navController.navigate(Screen.Quiz.createRoute(category))
                },
                onRevisionClick = {
                    navController.navigate(Screen.Revision.route)
                },
                onStatsClick = {
                    navController.navigate(Screen.Statistics.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("categorySlug") { type = NavType.StringType })
        ) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("categorySlug").orEmpty()
            val category = Category.fromSlug(slug) ?: Category.SYNONYM

            QuizScreen(
                category = category,
                questionIndex = 0,
                totalQuestions = 10,
                question = previewSampleQuestion.copy(category = category),
                onAnswerSubmitted = {},
                onContinueClick = {
                    navController.navigate(Screen.SectionResult.createRoute(category, 10, 10))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SectionResult.route,
            arguments = listOf(
                navArgument("categorySlug") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("categorySlug").orEmpty()
            val category = Category.fromSlug(slug) ?: Category.SYNONYM
            val score = backStackEntry.arguments?.getInt("score") ?: 10
            val total = backStackEntry.arguments?.getInt("total") ?: 10

            SectionResultScreen(
                category = category,
                score = score,
                total = total,
                pointsEarned = score * 10,
                isStreakMaintained = score == total,
                onContinueClick = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        composable(Screen.DailySummary.route) {
            DailySummaryScreen(
                totalScore = 27,
                totalQuestions = 30,
                totalPointsEarned = 270,
                synonymScore = 10,
                antonymScore = 9,
                idiomScore = 8,
                isGigaStreakAchieved = false,
                gigaStreakCount = 0,
                totalPoints = 13110L,
                onDoneClick = {
                    navController.popBackStack(Screen.Home.route, false)
                }
            )
        }

        composable(Screen.Revision.route) {
            RevisionScreen(
                questionIndex = 0,
                totalQuestions = 24,
                question = previewSampleQuestion,
                onAnswerSubmitted = {},
                onContinueClick = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                stats = UserStats(
                    totalPoints = 12840L,
                    synonymCurrentStreak = 12,
                    synonymBestStreak = 14,
                    antonymCurrentStreak = 9,
                    antonymBestStreak = 10,
                    idiomCurrentStreak = 4,
                    idiomBestStreak = 7,
                    gigaCurrentStreak = 7,
                    gigaBestStreak = 14,
                    totalQuestionsAnswered = 1420,
                    totalCorrectAnswers = 1230,
                    perfectSectionsCount = 98,
                    perfectDaysCount = 18
                ),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}