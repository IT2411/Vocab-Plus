package com.vocabplus.app.core.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vocabplus.app.VocabApplication
import com.vocabplus.app.core.util.DateUtils
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.presentation.home.HomeScreen
import com.vocabplus.app.presentation.home.HomeViewModel
import com.vocabplus.app.presentation.quiz.QuizScreen
import com.vocabplus.app.presentation.quiz.QuizUiState
import com.vocabplus.app.presentation.quiz.QuizViewModel
import com.vocabplus.app.presentation.result.DailySummaryScreen
import com.vocabplus.app.presentation.result.DailySummaryViewModel
import com.vocabplus.app.presentation.result.SectionResultScreen
import com.vocabplus.app.presentation.revision.RevisionCompletedScreen
import com.vocabplus.app.presentation.revision.RevisionEmptyScreen
import com.vocabplus.app.presentation.revision.RevisionScreen
import com.vocabplus.app.presentation.revision.RevisionUiState
import com.vocabplus.app.presentation.revision.RevisionViewModel
import com.vocabplus.app.presentation.settings.SettingsScreen
import com.vocabplus.app.presentation.settings.SettingsViewModel
import com.vocabplus.app.presentation.statistics.StatisticsScreen
import com.vocabplus.app.presentation.statistics.StatisticsViewModel

@Composable
fun VocabNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current.applicationContext as VocabApplication
    val container = context.container
    val todayIso = DateUtils.todayIso()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() },
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(
                    quizRepository = container.quizRepository,
                    userProgressRepository = container.userProgressRepository,
                    dateIso = todayIso
                )
            )
            val homeState by homeViewModel.uiState.collectAsState()

            HomeScreen(
                totalPoints = homeState.totalPoints,
                gigaStreak = homeState.gigaStreak,
                synonymStreak = homeState.synonymStreak,
                antonymStreak = homeState.antonymStreak,
                idiomStreak = homeState.idiomStreak,
                synonymState = homeState.synonymState,
                antonymState = homeState.antonymState,
                idiomState = homeState.idiomState,
                isDailyCompleted = homeState.isDailyCompleted,
                revisionQuestionsCount = homeState.revisionQuestionsCount,
                onCategoryClick = { category ->
                    navController.navigate(Screen.Quiz.createRoute(category))
                },
                onDailySummaryClick = {
                    navController.navigate(Screen.DailySummary.route)
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

            val quizViewModel: QuizViewModel = viewModel(
                key = "$category-$todayIso",
                factory = QuizViewModel.provideFactory(
                    category = category,
                    dateIso = todayIso,
                    quizRepository = container.quizRepository,
                    userProgressRepository = container.userProgressRepository
                )
            )
            val state by quizViewModel.uiState.collectAsState()

            when (val current = state) {
                is QuizUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is QuizUiState.Active -> {
                    QuizScreen(
                        category = current.category,
                        questionIndex = current.currentIndex,
                        totalQuestions = current.totalQuestions,
                        question = current.currentQuestion,
                        onAnswerSubmitted = { selectedIndex ->
                            quizViewModel.onOptionSelected(selectedIndex)
                        },
                        onContinueClick = {
                            quizViewModel.onContinue()
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }
                is QuizUiState.Completed -> {
                    SectionResultScreen(
                        category = current.category,
                        score = current.score,
                        total = current.totalQuestions,
                        pointsEarned = current.pointsEarned,
                        isStreakMaintained = current.isStreakMaintained,
                        onContinueClick = {
                            navController.popBackStack(Screen.Home.route, false)
                        }
                    )
                }
                is QuizUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = current.message)
                    }
                }
            }
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
            val dailySummaryViewModel: DailySummaryViewModel = viewModel(
                factory = DailySummaryViewModel.provideFactory(
                    quizRepository = container.quizRepository,
                    userProgressRepository = container.userProgressRepository,
                    dateIso = todayIso
                )
            )
            val summaryState by dailySummaryViewModel.uiState.collectAsState()

            if (summaryState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                DailySummaryScreen(
                    totalScore = summaryState.totalScore,
                    totalQuestions = summaryState.totalQuestions,
                    totalPointsEarned = summaryState.totalPointsEarned,
                    synonymScore = summaryState.synonymScore,
                    antonymScore = summaryState.antonymScore,
                    idiomScore = summaryState.idiomScore,
                    isGigaStreakAchieved = summaryState.isGigaStreakAchieved,
                    gigaStreakCount = summaryState.gigaStreakCount,
                    totalPoints = summaryState.totalPoints,
                    onDoneClick = {
                        navController.popBackStack(Screen.Home.route, false)
                    }
                )
            }
        }

        composable(Screen.Revision.route) {
            val revisionViewModel: RevisionViewModel = viewModel(
                factory = RevisionViewModel.provideFactory(
                    questionRepository = container.questionRepository,
                    userProgressRepository = container.userProgressRepository
                )
            )
            val state by revisionViewModel.uiState.collectAsState()

            when (val current = state) {
                is RevisionUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is RevisionUiState.Empty -> {
                    RevisionEmptyScreen(onBackClick = { navController.popBackStack() })
                }
                is RevisionUiState.Active -> {
                    RevisionScreen(
                        questionIndex = current.currentIndex,
                        totalQuestions = current.totalQuestions,
                        question = current.currentQuestion,
                        selectedOptionIndex = current.selectedOptionIndex,
                        isAnswerEvaluated = current.isAnswerEvaluated,
                        onOptionSelected = { revisionViewModel.onOptionSelected(it) },
                        onContinueClick = { revisionViewModel.onContinue() },
                        onBackClick = { navController.popBackStack() }
                    )
                }
                is RevisionUiState.Completed -> {
                    RevisionCompletedScreen(
                        score = current.score,
                        totalQuestions = current.totalQuestions,
                        bonusPointsEarned = current.bonusPointsEarned,
                        onDoneClick = { navController.popBackStack(Screen.Home.route, false) }
                    )
                }
                is RevisionUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = current.message)
                    }
                }
            }
        }

        composable(Screen.Statistics.route) {
            val statsViewModel: StatisticsViewModel = viewModel(
                factory = StatisticsViewModel.provideFactory(
                    database = container.database,
                    userProgressRepository = container.userProgressRepository
                )
            )
            val statsState by statsViewModel.uiState.collectAsState()

            StatisticsScreen(
                stats = statsState.overallStats,
                synonymPerformance = statsState.synonymPerformance,
                antonymPerformance = statsState.antonymPerformance,
                idiomPerformance = statsState.idiomPerformance,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.provideFactory(
                    preferencesRepository = container.userPreferencesRepository
                )
            )
            val settingsState by settingsViewModel.uiState.collectAsState()

            SettingsScreen(
                state = settingsState,
                onThemeSelected = { settingsViewModel.onThemeSelected(it) },
                onNotificationsToggled = { settingsViewModel.onNotificationToggled(it) },
                onShowResetDialog = { settingsViewModel.onShowResetDialog() },
                onDismissResetDialog = { settingsViewModel.onDismissResetDialog() },
                onConfirmReset = { settingsViewModel.onConfirmReset() },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}