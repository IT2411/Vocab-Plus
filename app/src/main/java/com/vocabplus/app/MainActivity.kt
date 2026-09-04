package com.vocabplus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vocabplus.app.core.designsystem.VocabTheme
import com.vocabplus.app.core.navigation.VocabNavHost
import com.vocabplus.app.domain.model.ThemePreference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (applicationContext as VocabApplication).container

        setContent {
            val themePreference by container.userPreferencesRepository
                .getThemePreference()
                .collectAsState(initial = ThemePreference.SYSTEM)

            val isDarkTheme = when (themePreference) {
                ThemePreference.SYSTEM -> isSystemInDarkTheme()
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
            }

            VocabTheme(darkTheme = isDarkTheme) {
                VocabNavHost()
            }
        }
    }
}