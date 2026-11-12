package com.example.lab5mobile.domain.usecase

import com.example.lab5mobile.domain.model.DepartureBoard
import com.example.lab5mobile.domain.model.Flight
import com.example.lab5mobile.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Рейсы из аэропорта: направления из базы плюс отметка избранного.
 * Поток пересчитывается сам, когда меняется таблица favorite.
 */
class GetDepartureBoardUseCase(private val repository: FlightRepository) {

    operator fun invoke(departureCode: String): Flow<DepartureBoard?> {
        val code = departureCode.trim().uppercase()
        return combine(
            repository.getAirport(code),
            repository.getDestinations(code),
            repository.getFavoriteDestinationCodes(code)
        ) { departure, destinations, favoriteCodes ->
            departure?.let {
                DepartureBoard(
                    departure = it,
                    flights = destinations.map { destination ->
                        Flight(
                            departure = it,
                            destination = destination,
                            isFavorite = destination.iataCode in favoriteCodes
                        )
                    }
                )
            }
        }
    }
}
