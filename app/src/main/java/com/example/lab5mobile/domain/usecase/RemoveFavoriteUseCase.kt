package com.example.lab5mobile.domain.usecase

import com.example.lab5mobile.domain.repository.FlightRepository

/**
 * Удаление маршрута из списка избранного: здесь звёздочка только убирает.
 */
class RemoveFavoriteUseCase(private val repository: FlightRepository) {

    suspend operator fun invoke(departureCode: String, destinationCode: String) {
        repository.removeFavorite(departureCode, destinationCode)
    }
}
