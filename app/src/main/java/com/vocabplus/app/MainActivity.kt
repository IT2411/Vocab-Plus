package com.vocabplus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vocabplus.app.core.designsystem.VocabTheme
import com.vocabplus.app.core.navigation.VocabNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocabTheme {
                VocabNavHost()
            }
        }
    }
}