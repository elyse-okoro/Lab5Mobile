package com.example.lab5mobile.data.repository

import com.example.lab5mobile.data.local.AirportDao
import com.example.lab5mobile.data.local.FavoriteDao
import com.example.lab5mobile.data.local.FavoriteEntity
import com.example.lab5mobile.data.local.toDomain
import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Репозиторий поверх локальной базы Room. Запросы идут в базу по мере надобности,
 * целиком таблицы в память не загружаются.
 */
class OfflineFlightRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao
) : FlightRepository {

    override fun searchAirports(query: String): Flow<List<Airport>> {
        val escaped = query
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
        return airportDao.searchAirports(codePrefix = "$escaped%", namePart = "%$escaped%")
            .map { airports -> airports.map { it.toDomain() } }
    }

    override fun getAirport(code: String): Flow<Airport?> =
        airportDao.getAirportByCode(code).map { it?.toDomain() }

    override fun getDestinations(departureCode: String): Flow<List<Airport>> =
        airportDao.getDestinations(departureCode).map { airports -> airports.map { it.toDomain() } }

    override fun getFavoriteRoutes(): Flow<List<FavoriteRoute>> =
        favoriteDao.getFavoriteRoutes().map { routes -> routes.map { it.toDomain() } }

    override fun getFavoriteDestinationCodes(departureCode: String): Flow<Set<String>> =
        favoriteDao.getFavoriteDestinationCodes(departureCode).map { it.toSet() }

    override suspend fun addFavorite(departureCode: String, destinationCode: String) {
        if (favoriteDao.countRoute(departureCode, destinationCode) == 0) {
            favoriteDao.insert(
                FavoriteEntity(departureCode = departureCode, destinationCode = destinationCode)
            )
        }
    }

    override suspend fun removeFavorite(departureCode: String, destinationCode: String) {
        favoriteDao.deleteRoute(departureCode, destinationCode)
    }
}
