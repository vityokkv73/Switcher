package com.deerhunter.switcher.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences as DataStorePreferences

private val Context.dataStore: DataStore<DataStorePreferences> by preferencesDataStore(
    name = "secure_preferences"
)

@Singleton
class Preferences @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptionHelper: EncryptionHelper
) {

    suspend fun saveString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = encryptionHelper.encryptData(value)
        }
    }

    fun getStringFlow(key: String, defaultValue: String = ""): Flow<String> {
        val preferencesKey = stringPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            val encryptedValue = preferences[preferencesKey]
            if (encryptedValue != null) {
                runCatching { encryptionHelper.decryptData(encryptedValue) }.getOrElse { defaultValue }
            } else {
                defaultValue
            }
        }
    }

    suspend fun getString(key: String, defaultValue: String = ""): String {
        return getStringFlow(key, defaultValue).first()
    }

    suspend fun removeKey(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences.remove(preferencesKey)
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
