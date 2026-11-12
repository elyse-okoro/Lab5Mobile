package com.example.lab5mobile.domain.repository

import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.model.FavoriteRoute
import kotlinx.coroutines.flow.Flow

interface FlightRepository {

    fun searchAirports(query: String): Flow<List<Airport>>

    fun getAirport(code: String): Flow<Airport?>

    fun getDestinations(departureCode: String): Flow<List<Airport>>

    fun getFavoriteRoutes(): Flow<List<FavoriteRoute>>

    fun getFavoriteDestinationCodes(departureCode: String): Flow<Set<String>>

    /** Добавляет маршрут в избранное или убирает его, если он уже там. Решение — по данным базы. */
    suspend fun toggleFavorite(departureCode: String, destinationCode: String)

    suspend fun removeFavorite(departureCode: String, destinationCode: String)
}
