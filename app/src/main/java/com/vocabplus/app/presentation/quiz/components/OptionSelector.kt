package com.vocabplus.app.presentation.quiz.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.MutedNotice
import com.vocabplus.app.core.designsystem.MutedNoticeLight
import com.vocabplus.app.core.designsystem.MutedSuccess
import com.vocabplus.app.core.designsystem.MutedSuccessLight

enum class OptionState {
    DEFAULT,
    SELECTED,
    CORRECT,
    INCORRECT
}

@Composable
fun OptionSelector(
    options: List<String>,
    selectedOptionIndex: Int?,
    correctOptionIndex: Int?,
    isAnswerEvaluated: Boolean,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf("A", "B", "C", "D")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEachIndexed { index, optionText ->
            val optionState = when {
                !isAnswerEvaluated && selectedOptionIndex == index -> OptionState.SELECTED
                isAnswerEvaluated && index == correctOptionIndex -> OptionState.CORRECT
                isAnswerEvaluated && selectedOptionIndex == index && index != correctOptionIndex -> OptionState.INCORRECT
                else -> OptionState.DEFAULT
            }

            OptionRow(
                label = labels.getOrElse(index) { "" },
                text = optionText,
                state = optionState,
                onClick = {
                    if (!isAnswerEvaluated) {
                        onOptionSelected(index)
                    }
                }
            )
        }
    }
}

@Composable
private fun OptionRow(
    label: String,
    text: String,
    state: OptionState,
    onClick: () -> Unit
) {
    val (containerColor, contentColor, borderColor) = when (state) {
        OptionState.DEFAULT -> Triple(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.outline
        )
        OptionState.SELECTED -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.primary
        )
        OptionState.CORRECT -> Triple(
            MutedSuccessLight,
            MutedSuccess,
            MutedSuccess
        )
        OptionState.INCORRECT -> Triple(
            MutedNoticeLight,
            MutedNotice,
            MutedNotice
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label.",
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }
    }
}