package com.example.lab5mobile.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.lab5mobile.data.local.FlightDatabase
import com.example.lab5mobile.data.preferences.SearchPreferencesRepository
import com.example.lab5mobile.data.repository.OfflineFlightRepository
import com.example.lab5mobile.domain.repository.FlightRepository
import com.example.lab5mobile.domain.repository.SearchQueryRepository

private const val SEARCH_PREFERENCES_NAME = "search_preferences"

private val Context.searchDataStore: DataStore<Preferences> by preferencesDataStore(
    name = SEARCH_PREFERENCES_NAME
)

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val flightRepository: FlightRepository by lazy {
        val database = FlightDatabase.getDatabase(context)
        OfflineFlightRepository(database.airportDao(), database.favoriteDao())
    }

    override val searchQueryRepository: SearchQueryRepository by lazy {
        SearchPreferencesRepository(context.searchDataStore)
    }
}
