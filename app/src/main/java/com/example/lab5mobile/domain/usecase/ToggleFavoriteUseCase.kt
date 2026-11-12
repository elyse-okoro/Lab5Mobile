package com.example.lab5mobile.domain.usecase

import com.example.lab5mobile.domain.repository.FlightRepository

class ToggleFavoriteUseCase(private val repository: FlightRepository) {

    suspend operator fun invoke(departureCode: String, destinationCode: String, isFavorite: Boolean) {
        if (isFavorite) {
            repository.removeFavorite(departureCode, destinationCode)
        } else {
            repository.addFavorite(departureCode, destinationCode)
        }
    }
}
