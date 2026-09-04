package com.vocabplus.app.presentation.settings

import com.vocabplus.app.domain.model.ThemePreference

data class SettingsUiState(
    val theme: ThemePreference = ThemePreference.SYSTEM,
    val notificationsEnabled: Boolean = false,
    val showResetConfirmationDialog: Boolean = false
)