package com.vocabplus.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vocabplus.app.domain.model.ThemePreference
import com.vocabplus.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesRepository.getThemePreference(),
                preferencesRepository.getNotificationsEnabled()
            ) { theme, notificationsEnabled ->
                _uiState.value.copy(
                    theme = theme,
                    notificationsEnabled = notificationsEnabled
                )
            }.collect { updated ->
                _uiState.value = updated
            }
        }
    }

    fun onThemeSelected(theme: ThemePreference) {
        viewModelScope.launch {
            preferencesRepository.setThemePreference(theme)
        }
    }

    fun onNotificationToggled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setNotificationsEnabled(enabled)
        }
    }

    fun onShowResetDialog() {
        _uiState.value = _uiState.value.copy(showResetConfirmationDialog = true)
    }

    fun onDismissResetDialog() {
        _uiState.value = _uiState.value.copy(showResetConfirmationDialog = false)
    }

    fun onConfirmReset() {
        viewModelScope.launch {
            preferencesRepository.resetAllData()
            _uiState.value = _uiState.value.copy(showResetConfirmationDialog = false)
        }
    }

    companion object {
        fun provideFactory(
            preferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(preferencesRepository) as T
            }
        }
    }
}