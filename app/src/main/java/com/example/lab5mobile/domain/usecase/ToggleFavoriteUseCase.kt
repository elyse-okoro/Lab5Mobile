package com.example.lab5mobile.domain.usecase

import com.example.lab5mobile.domain.repository.FlightRepository

/**
 * Звёздочка у рейса. Что сделать — добавить или убрать — решает база, а не карточка:
 * при быстром повторном нажатии карточка ещё может показывать старое состояние.
 */
class ToggleFavoriteUseCase(private val repository: FlightRepository) {

    suspend operator fun invoke(departureCode: String, destinationCode: String) {
        repository.toggleFavorite(departureCode, destinationCode)
    }
}
