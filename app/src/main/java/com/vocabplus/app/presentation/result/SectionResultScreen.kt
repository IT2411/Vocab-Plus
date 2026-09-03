package com.vocabplus.app.presentation.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.AccentAmber
import com.vocabplus.app.core.designsystem.components.VocabPrimaryButton
import com.vocabplus.app.core.designsystem.components.VocabTopAppBar
import com.vocabplus.app.domain.model.Category

@Composable
fun SectionResultScreen(
    category: Category,
    score: Int,
    total: Int,
    pointsEarned: Int,
    isStreakMaintained: Boolean,
    onContinueClick: () -> Unit,
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
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "${category.displayName.uppercase()} COMPLETE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$score / $total",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "+$pointsEarned points",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isStreakMaintained) {
                Text(
                    text = "🔥 Streak continues",
                    style = MaterialTheme.typography.titleMedium,
                    color = AccentAmber
                )
            } else {
                Text(
                    text = "🔥 Streak reset",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Keep going.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            VocabPrimaryButton(
                text = "Back to Home",
                onClick = onContinueClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}