package com.example.lab5mobile.fake

import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Репозиторий в памяти с тем же поведением, что и запросы Room: LIKE по коду и названию,
 * сортировка по пассажиропотоку, реактивное избранное.
 */
class FakeFlightRepository(airports: List<Airport> = TestAirports.all) : FlightRepository {

    private val airportList = airports.sortedByDescending { it.passengers }
    private val favorites = MutableStateFlow<List<FavoriteRoute>>(emptyList())
    private var nextId = 1

    val favoriteCount: Int
        get() = favorites.value.size

    /** Имитация сбоя записи в базу. */
    var failOnSave = false

    override fun searchAirports(query: String): Flow<List<Airport>> = MutableStateFlow(
        airportList.filter {
            it.iataCode.startsWith(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
        }
    )

    override fun getAirport(code: String): Flow<Airport?> =
        MutableStateFlow(airportList.firstOrNull { it.iataCode.equals(code, ignoreCase = true) })

    override fun getDestinations(departureCode: String): Flow<List<Airport>> =
        MutableStateFlow(airportList.filter { it.iataCode != departureCode })

    override fun getFavoriteRoutes(): Flow<List<FavoriteRoute>> = favorites

    override fun getFavoriteDestinationCodes(departureCode: String): Flow<Set<String>> =
        favorites.map { routes ->
            routes.filter { it.departureCode == departureCode }.map { it.destinationCode }.toSet()
        }

    override suspend fun toggleFavorite(departureCode: String, destinationCode: String) {
        check(!failOnSave) { "disk is full" }
        val exists = favorites.value.any {
            it.departureCode == departureCode && it.destinationCode == destinationCode
        }
        if (exists) {
            removeFavorite(departureCode, destinationCode)
        } else {
            favorites.value = listOf(
                FavoriteRoute(nextId++, departureCode, null, destinationCode, null)
            ) + favorites.value
        }
    }

    override suspend fun removeFavorite(departureCode: String, destinationCode: String) {
        favorites.value = favorites.value.filterNot {
            it.departureCode == departureCode && it.destinationCode == destinationCode
        }
    }
}
