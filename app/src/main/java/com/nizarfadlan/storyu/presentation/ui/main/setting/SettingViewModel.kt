package com.nizarfadlan.storyu.presentation.ui.main.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nizarfadlan.storyu.data.pref.SettingPreferences
import com.nizarfadlan.storyu.domain.model.Language
import com.nizarfadlan.storyu.domain.model.ThemeMode
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<ThemeMode> = pref.getThemeSetting().asLiveData()
    fun getLanguageSettings(): LiveData<Language> = pref.getLanguageSetting().asLiveData()

    fun saveThemeSetting(themeMode: ThemeMode) {
        viewModelScope.launch {
            pref.saveThemeSetting(themeMode)
        }
    }

    fun saveLanguageSetting(language: Language) {
        viewModelScope.launch {
            pref.saveLanguageSetting(language)
        }
    }
}