package com.example.lab5mobile.data.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.lab5mobile.domain.repository.SearchQueryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Хранит последний поисковый запрос в Preferences DataStore.
 */
class SearchPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) : SearchQueryRepository {

    override val lastQuery: Flow<String> = dataStore.data
        .catch { error ->
            if (error is IOException) {
                Log.e(TAG, "Не удалось прочитать настройки поиска", error)
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences -> preferences[LAST_QUERY] ?: "" }

    override suspend fun saveLastQuery(query: String) {
        dataStore.edit { preferences -> preferences[LAST_QUERY] = query }
    }

    private companion object {
        const val TAG = "SearchPreferences"
        val LAST_QUERY = stringPreferencesKey("last_query")
    }
}
