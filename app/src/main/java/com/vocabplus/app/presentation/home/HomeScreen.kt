package com.vocabplus.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.AccentAmber
import com.vocabplus.app.core.designsystem.MutedSuccess
import com.vocabplus.app.core.designsystem.components.VocabCard
import com.vocabplus.app.core.designsystem.components.VocabPrimaryButton
import com.vocabplus.app.core.designsystem.components.VocabSecondaryButton
import com.vocabplus.app.core.designsystem.components.VocabTopAppBar
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.SectionState

@Composable
fun HomeScreen(
    totalPoints: Long,
    gigaStreak: Int,
    synonymStreak: Int,
    antonymStreak: Int,
    idiomStreak: Int,
    synonymState: SectionState,
    antonymState: SectionState,
    idiomState: SectionState,
    isDailyCompleted: Boolean,
    revisionQuestionsCount: Int,
    onCategoryClick: (Category) -> Unit,
    onDailySummaryClick: () -> Unit,
    onRevisionClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            VocabTopAppBar(
                title = "Vocab+",
                actions = {
                    IconButton(
                        onClick = onStatsClick,
                        modifier = Modifier.semantics { contentDescription = "View Statistics" }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BarChart,
                            contentDescription = "Statistics",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.semantics { contentDescription = "View Settings" }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Points Display
            Text(
                text = "%,d".format(totalPoints),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "permanent points",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Giga Streak Counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.semantics {
                    contentDescription = "$gigaStreak days Giga Streak"
                }
            ) {
                Text(text = "⚡", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$gigaStreak Giga Streak",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentAmber,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Daily Completed Banner
            if (isDailyCompleted) {
                Spacer(modifier = Modifier.height(16.dp))
                VocabCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = MutedSuccess
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "All 3 sections complete!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    VocabSecondaryButton(
                        text = "View Day Summary",
                        onClick = onDailySummaryClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(18.dp))

            // Daily Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "30 questions total",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Cards
            CategoryDailyCard(
                category = Category.SYNONYM,
                streak = synonymStreak,
                state = synonymState,
                onClick = { onCategoryClick(Category.SYNONYM) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CategoryDailyCard(
                category = Category.ANTONYM,
                streak = antonymStreak,
                state = antonymState,
                onClick = { onCategoryClick(Category.ANTONYM) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            CategoryDailyCard(
                category = Category.IDIOM,
                streak = idiomStreak,
                state = idiomState,
                onClick = { onCategoryClick(Category.IDIOM) }
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(18.dp))

            // Revision Entry
            VocabCard {
                Text(
                    text = "REVISION",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (revisionQuestionsCount > 0) "$revisionQuestionsCount questions ready" else "No questions ready yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(14.dp))
                VocabPrimaryButton(
                    text = "Start Revision",
                    onClick = onRevisionClick,
                    enabled = revisionQuestionsCount > 0
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CategoryDailyCard(
    category: Category,
    streak: Int,
    state: SectionState,
    onClick: () -> Unit
) {
    val stateDescription = when (state) {
        SectionState.COMPLETED -> "Completed"
        SectionState.IN_PROGRESS -> "In Progress"
        SectionState.NOT_STARTED -> "Not Started"
    }

    VocabCard(
        onClick = onClick,
        modifier = Modifier.semantics {
            contentDescription = "${category.displayName}, 10 questions, $stateDescription, $streak day streak"
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "10 questions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (state == SectionState.COMPLETED) {
                    Text(
                        text = "Completed ✓",
                        style = MaterialTheme.typography.labelLarge,
                        color = MutedSuccess,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔥", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$streak day streak",
                            style = MaterialTheme.typography.labelLarge,
                            color = AccentAmber
                        )
                    }
                }
            }
        }
    }
}