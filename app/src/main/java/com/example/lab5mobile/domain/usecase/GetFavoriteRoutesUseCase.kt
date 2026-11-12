package com.example.lab5mobile.domain.usecase

import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteRoutesUseCase(private val repository: FlightRepository) {

    operator fun invoke(): Flow<List<FavoriteRoute>> = repository.getFavoriteRoutes()
}
