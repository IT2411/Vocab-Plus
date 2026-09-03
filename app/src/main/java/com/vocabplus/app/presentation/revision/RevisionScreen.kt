package com.vocabplus.app.presentation.revision

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.components.VocabCard
import com.vocabplus.app.core.designsystem.components.VocabPrimaryButton
import com.vocabplus.app.core.designsystem.components.VocabTopAppBar
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.presentation.quiz.components.ExplanationCard
import com.vocabplus.app.presentation.quiz.components.OptionSelector

@Composable
fun RevisionScreen(
    questionIndex: Int,
    totalQuestions: Int,
    question: Question,
    selectedOptionIndex: Int?,
    isAnswerEvaluated: Boolean,
    onOptionSelected: (Int) -> Unit,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (questionIndex + 1).toFloat() / totalQuestions.toFloat()
    val isCorrect = selectedOptionIndex == question.correctOptionIndex

    Scaffold(
        topBar = {
            VocabTopAppBar(
                title = "Revision (${questionIndex + 1}/$totalQuestions)",
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
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mixed Category Tag (Synonyms, Antonyms, or Idioms)
            Text(
                text = question.category.displayName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Question Prompt
            Text(
                text = question.prompt,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            OptionSelector(
                options = question.options,
                selectedOptionIndex = selectedOptionIndex,
                correctOptionIndex = if (isAnswerEvaluated) question.correctOptionIndex else null,
                isAnswerEvaluated = isAnswerEvaluated,
                onOptionSelected = onOptionSelected
            )

            if (isAnswerEvaluated) {
                Spacer(modifier = Modifier.height(20.dp))
                ExplanationCard(
                    isCorrect = isCorrect,
                    correctAnswer = question.correctAnswer,
                    explanation = question.explanation,
                    exampleSentence = question.exampleSentence
                )

                Spacer(modifier = Modifier.height(24.dp))
                VocabPrimaryButton(
                    text = if (questionIndex + 1 >= totalQuestions) "Complete Revision" else "Continue",
                    onClick = onContinueClick
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RevisionEmptyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { VocabTopAppBar(title = "Revision", onBackClick = onBackClick) },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            VocabCard {
                Text(
                    text = "No Questions Ready",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Complete today's daily quiz sections. Encountered words will appear here for retrieval review.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(18.dp))
                VocabPrimaryButton(text = "Return Home", onClick = onBackClick)
            }
        }
    }
}

@Composable
fun RevisionCompletedScreen(
    score: Int,
    totalQuestions: Int,
    bonusPointsEarned: Int,
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "REVISION COMPLETE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$score / $totalQuestions",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "+$bonusPointsEarned bonus points",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            VocabPrimaryButton(text = "Done", onClick = onDoneClick)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}