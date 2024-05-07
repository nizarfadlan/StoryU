package com.nizarfadlan.storyu.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nizarfadlan.storyu.domain.model.Language
import com.nizarfadlan.storyu.domain.model.ThemeMode
import com.nizarfadlan.storyu.utils.constant.AppConstant.PREFS_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreSetting: DataStore<Preferences> by preferencesDataStore(name = PREFS_SETTINGS)

class SettingPreferences(context: Context) {
    private val dataStore = context.dataStoreSetting

    private val themeKey = stringPreferencesKey("theme_setting")
    private val languageKey = stringPreferencesKey("language_setting")

    fun getThemeSetting(): Flow<ThemeMode> {
        return dataStore.data.map { preferences ->
            val themeString = preferences[themeKey] ?: ThemeMode.SYSTEM.name
            ThemeMode.valueOf(themeString)
        }
    }

    suspend fun saveThemeSetting(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[themeKey] = themeMode.name
        }
    }

    fun getLanguageSetting(): Flow<Language> {
        return dataStore.data.map { preferences ->
            val languageString = preferences[languageKey] ?: Language.SYSTEM.name
            Language.valueOf(languageString)
        }
    }

    suspend fun saveLanguageSetting(language: Language) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language.name
        }
    }

}