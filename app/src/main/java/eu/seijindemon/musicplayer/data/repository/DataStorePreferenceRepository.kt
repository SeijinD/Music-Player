package eu.seijindemon.musicplayer.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreferenceRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val languageDataStore: DataStore<Preferences> = context.createDataStore(name = "LanguageData")
    private val defaultLanguage = 0

    companion object {
        val PREF_LANGUAGE = preferencesKey<Int>("language")

        private var INSTANCE: DataStorePreferenceRepository? = null

        fun getInstance(context: Context): DataStorePreferenceRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }
                val instance = DataStorePreferenceRepository(context = context)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun setLanguage(language: Int) {
        languageDataStore.edit { preferences ->
            preferences[PREF_LANGUAGE] = language
        }
    }

    val getLanguage: Flow<Int> = languageDataStore.data
        .map {  preferences ->
            preferences[PREF_LANGUAGE] ?: defaultLanguage
        }
}