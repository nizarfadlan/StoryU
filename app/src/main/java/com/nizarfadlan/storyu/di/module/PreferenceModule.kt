package com.nizarfadlan.storyu.di.module

import com.nizarfadlan.storyu.data.pref.SettingPreferences
import com.nizarfadlan.storyu.data.pref.UserSessionPreference
import org.koin.dsl.module

val preferenceModule = module {
    single { UserSessionPreference(get()) }
    single { SettingPreferences(get()) }
}