package com.vocabplus.app.presentation.quiz

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocabplus.app.core.designsystem.components.VocabPrimaryButton
import com.vocabplus.app.core.designsystem.components.VocabTopAppBar
import com.vocabplus.app.domain.model.Category
import com.vocabplus.app.domain.model.Question
import com.vocabplus.app.presentation.quiz.components.ExplanationCard
import com.vocabplus.app.presentation.quiz.components.OptionSelector

@Composable
fun QuizScreen(
    category: Category,
    questionIndex: Int,
    totalQuestions: Int,
    question: Question,
    onAnswerSubmitted: (selectedOptionIndex: Int) -> Unit,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOptionIndex by remember(question.id) { mutableStateOf<Int?>(null) }
    var isAnswerEvaluated by remember(question.id) { mutableStateOf(false) }

    val progress = (questionIndex + 1).toFloat() / totalQuestions.toFloat()
    val isCorrect = selectedOptionIndex == question.correctOptionIndex

    Scaffold(
        topBar = {
            VocabTopAppBar(
                title = "${category.displayName} (${questionIndex + 1}/$totalQuestions)",
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

            Spacer(modifier = Modifier.height(28.dp))

            // Question Prompt
            Text(
                text = question.prompt,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 4 MCQ Options
            OptionSelector(
                options = question.options,
                selectedOptionIndex = selectedOptionIndex,
                correctOptionIndex = if (isAnswerEvaluated) question.correctOptionIndex else null,
                isAnswerEvaluated = isAnswerEvaluated,
                onOptionSelected = { index ->
                    selectedOptionIndex = index
                    isAnswerEvaluated = true
                    onAnswerSubmitted(index)
                }
            )

            // Explanation & Example Card (Appears after answer)
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
                    text = if (questionIndex + 1 >= totalQuestions) "Complete Section" else "Continue",
                    onClick = onContinueClick
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}