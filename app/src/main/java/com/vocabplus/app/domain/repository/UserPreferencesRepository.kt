package com.vocabplus.app.domain.repository

import com.vocabplus.app.domain.model.ThemePreference
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getThemePreference(): Flow<ThemePreference>
    suspend fun setThemePreference(theme: ThemePreference)

    fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)

    suspend fun resetAllData()
}