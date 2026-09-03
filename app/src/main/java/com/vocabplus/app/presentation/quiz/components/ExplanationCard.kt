package com.vocabplus.app.presentation.quiz.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.MutedNotice
import com.vocabplus.app.core.designsystem.MutedSuccess
import com.vocabplus.app.core.designsystem.components.VocabCard

@Composable
fun ExplanationCard(
    isCorrect: Boolean,
    correctAnswer: String,
    explanation: String,
    exampleSentence: String,
    modifier: Modifier = Modifier
) {
    VocabCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                contentDescription = if (isCorrect) "Correct" else "Not quite",
                tint = if (isCorrect) MutedSuccess else MutedNotice
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isCorrect) "Correct" else "Not quite",
                style = MaterialTheme.typography.titleMedium,
                color = if (isCorrect) MutedSuccess else MutedNotice,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isCorrect) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Correct answer: $correctAnswer",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = explanation,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Example:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "\"$exampleSentence\"",
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}