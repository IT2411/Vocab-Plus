package com.vocabplus.app.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.components.VocabCard
import com.vocabplus.app.core.designsystem.components.VocabTopAppBar
import com.vocabplus.app.domain.model.UserStats

@Composable
fun StatisticsScreen(
    stats: UserStats,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            VocabTopAppBar(
                title = "Statistics",
                onBackClick = onBackClick
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OVERALL",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            VocabCard {
                StatRow("Total points", "%,d".format(stats.totalPoints))
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Questions answered", "${stats.totalQuestionsAnswered}")
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Overall accuracy", "%.1f%%".format(stats.overallAccuracy))
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Perfect sections", "${stats.perfectSectionsCount}")
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Perfect days", "${stats.perfectDaysCount}")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "STREAKS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            VocabCard {
                StatRow("Current Synonym streak", "${stats.synonymCurrentStreak}")
                StatRow("Best Synonym streak", "${stats.synonymBestStreak}")
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Current Antonym streak", "${stats.antonymCurrentStreak}")
                StatRow("Best Antonym streak", "${stats.antonymBestStreak}")
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Current Idiom streak", "${stats.idiomCurrentStreak}")
                StatRow("Best Idiom streak", "${stats.idiomBestStreak}")
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("Current Giga streak", "${stats.gigaCurrentStreak}")
                StatRow("Best Giga streak", "${stats.gigaBestStreak}")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}