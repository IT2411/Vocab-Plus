package com.vocabplus.app.core.di

import android.content.Context
import com.vocabplus.app.core.util.DefaultDispatcherProvider
import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.data.datasource.AssetQuestionDataSource
import com.vocabplus.app.data.datasource.AssetQuestionDataSourceImpl
import com.vocabplus.app.data.repository.InMemoryUserProgressRepository
import com.vocabplus.app.data.repository.QuestionRepositoryImpl
import com.vocabplus.app.data.repository.QuizRepositoryImpl
import com.vocabplus.app.domain.repository.QuestionRepository
import com.vocabplus.app.domain.repository.QuizRepository
import com.vocabplus.app.domain.repository.UserProgressRepository

interface AppContainer {
    val dispatchers: DispatcherProvider
    val questionRepository: QuestionRepository
    val quizRepository: QuizRepository
    val userProgressRepository: UserProgressRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val dispatchers: DispatcherProvider by lazy {
        DefaultDispatcherProvider()
    }

    private val assetDataSource: AssetQuestionDataSource by lazy {
        AssetQuestionDataSourceImpl(context)
    }

    override val questionRepository: QuestionRepository by lazy {
        QuestionRepositoryImpl(assetDataSource, dispatchers)
    }

    override val userProgressRepository: UserProgressRepository by lazy {
        InMemoryUserProgressRepository(questionRepository, dispatchers)
    }

    override val quizRepository: QuizRepository by lazy {
        QuizRepositoryImpl(questionRepository, dispatchers)
    }
}