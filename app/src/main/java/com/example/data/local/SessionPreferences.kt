package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.gorphinDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "gorphin_prefs"
)

data class StoredSession(
    val token: String,
    val uiLang: String,
    val thinkMode: String,
    val webSearch: Boolean
)

class SessionPreferences(private val context: Context) {
    private object Keys {
        val AUTH_TOKEN = stringPreferencesKey("gorphin_auth_token")
        val UI_LANG = stringPreferencesKey("gorphin_ui_lang")
        val THINK_MODE = stringPreferencesKey("gorphin_think_mode")
        val WEB_SEARCH = booleanPreferencesKey("gorphin_web_search")
    }

    private fun defaultLanguage(): String {
        return when (Locale.getDefault().language.lowercase()) {
            "ru" -> "ru"
            "uk" -> "uk"
            else -> "en"
        }
    }

    val sessionFlow: Flow<StoredSession> = context.gorphinDataStore.data.map { prefs ->
        StoredSession(
            token = prefs[Keys.AUTH_TOKEN] ?: "",
            uiLang = prefs[Keys.UI_LANG] ?: defaultLanguage(),
            thinkMode = prefs[Keys.THINK_MODE] ?: "auto",
            webSearch = prefs[Keys.WEB_SEARCH] ?: false
        )
    }

    suspend fun getSessionSnapshot(): StoredSession = sessionFlow.first()

    suspend fun saveToken(token: String) {
        context.gorphinDataStore.edit { it[Keys.AUTH_TOKEN] = token }
    }

    suspend fun clearToken() {
        context.gorphinDataStore.edit { it.remove(Keys.AUTH_TOKEN) }
    }

    suspend fun saveLanguage(lang: String) {
        context.gorphinDataStore.edit { it[Keys.UI_LANG] = lang }
    }

    suspend fun saveThinkMode(mode: String) {
        context.gorphinDataStore.edit { it[Keys.THINK_MODE] = mode }
    }

    suspend fun saveWebSearch(enabled: Boolean) {
        context.gorphinDataStore.edit { it[Keys.WEB_SEARCH] = enabled }
    }
}
