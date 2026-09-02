package com.vocabplus.app.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MutedNavy,
    onPrimary = Sand50,
    primaryContainer = Sand100,
    onPrimaryContainer = Slate900,
    background = Sand50,
    onBackground = Slate900,
    surface = Sand100,
    onSurface = Slate900,
    surfaceVariant = Sand200,
    onSurfaceVariant = Slate700,
    outline = Sand200
)

private val DarkColorScheme = darkColorScheme(
    primary = Slate300,
    onPrimary = Charcoal950,
    primaryContainer = Charcoal900,
    onPrimaryContainer = Charcoal50,
    background = Charcoal950,
    onBackground = Charcoal50,
    surface = Charcoal900,
    onSurface = Charcoal50,
    surfaceVariant = Charcoal800,
    onSurfaceVariant = Charcoal200,
    outline = Charcoal800
)

@Composable
fun VocabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VocabTypography,
        shapes = VocabShapes,
        content = content
    )
}