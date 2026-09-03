package com.vocabplus.app.presentation.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.AccentAmber
import com.vocabplus.app.core.designsystem.components.VocabCard
import com.vocabplus.app.core.designsystem.components.VocabPrimaryButton
import com.vocabplus.app.core.designsystem.components.VocabTopAppBar

@Composable
fun DailySummaryScreen(
    totalScore: Int,
    totalQuestions: Int,
    totalPointsEarned: Int,
    synonymScore: Int,
    antonymScore: Int,
    idiomScore: Int,
    isGigaStreakAchieved: Boolean,
    gigaStreakCount: Int,
    totalPoints: Long,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { VocabTopAppBar(title = "") },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TODAY COMPLETE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$totalScore / $totalQuestions",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "+$totalPointsEarned points",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            VocabCard {
                SectionScoreRow("Synonyms", synonymScore, 10)
                Spacer(modifier = Modifier.height(8.dp))
                SectionScoreRow("Antonyms", antonymScore, 10)
                Spacer(modifier = Modifier.height(8.dp))
                SectionScoreRow("Idioms", idiomScore, 10)
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isGigaStreakAchieved) {
                Text(
                    text = "⚡ GIGA STREAK: $gigaStreakCount",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentAmber
                )
            } else {
                Text(
                    text = "⚡ Giga Streak reset",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total points",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "%,d".format(totalPoints),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))

            VocabPrimaryButton(
                text = "Finish",
                onClick = onDoneClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionScoreRow(title: String, score: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$score / $total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (score == total) {
                Text(text = " 🔥", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}