package com.nizarfadlan.storyu.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nizarfadlan.storyu.domain.model.User
import com.nizarfadlan.storyu.utils.constant.AppConstant.PREFS_SESSIONS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStoreSession: DataStore<Preferences> by preferencesDataStore(name = PREFS_SESSIONS)

class UserSessionPreference(context: Context) {
    private val dataStore = context.dataStoreSession

    private val idKey = stringPreferencesKey("userId")
    private val nameKey = stringPreferencesKey("name")
    private val tokenKey = stringPreferencesKey("token")


    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[idKey] = user.id
            preferences[nameKey] = user.name
            preferences[tokenKey] = user.token
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[idKey] ?: "",
                preferences[nameKey] ?: "",
                preferences[tokenKey] ?: ""
            )
        }
    }

    suspend fun getToken(): String = dataStore.data.first()[tokenKey] ?: ""

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}