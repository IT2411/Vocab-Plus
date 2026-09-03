package com.vocabplus.app

import android.app.Application
import com.vocabplus.app.core.di.AppContainer
import com.vocabplus.app.core.di.DefaultAppContainer

class VocabApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}