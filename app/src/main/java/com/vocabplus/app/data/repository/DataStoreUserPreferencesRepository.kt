package com.vocabplus.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vocabplus.app.core.notification.DailyReminderWorker
import com.vocabplus.app.core.util.DispatcherProvider
import com.vocabplus.app.data.local.VocabDatabase
import com.vocabplus.app.data.local.entity.UserStatsEntity
import com.vocabplus.app.domain.model.ThemePreference
import com.vocabplus.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreUserPreferencesRepository(
    private val context: Context,
    private val database: VocabDatabase,
    private val dispatchers: DispatcherProvider
) : UserPreferencesRepository {

    private val themeKey = stringPreferencesKey("app_theme")
    private val notificationKey = booleanPreferencesKey("daily_notifications_enabled")

    override fun getThemePreference(): Flow<ThemePreference> {
        return context.dataStore.data.map { preferences ->
            val name = preferences[themeKey] ?: ThemePreference.SYSTEM.name
            try {
                ThemePreference.valueOf(name)
            } catch (_: Exception) {
                ThemePreference.SYSTEM
            }
        }
    }

    override suspend fun setThemePreference(theme: ThemePreference) {
        withContext(dispatchers.io) {
            context.dataStore.edit { preferences ->
                preferences[themeKey] = theme.name
            }
        }
    }

    override fun getNotificationsEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[notificationKey] ?: false
        }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        withContext(dispatchers.io) {
            context.dataStore.edit { preferences ->
                preferences[notificationKey] = enabled
            }
            if (enabled) {
                DailyReminderWorker.schedule(context)
            } else {
                DailyReminderWorker.cancel(context)
            }
        }
    }

    override suspend fun resetAllData() {
        withContext(dispatchers.io) {
            database.clearAllTables()
            database.userStatsDao().insertOrUpdate(UserStatsEntity(id = 1))
        }
    }
}