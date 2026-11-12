package com.example.lab5mobile.domain.usecase

import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SearchAirportsUseCase(private val repository: FlightRepository) {

    operator fun invoke(query: String): Flow<List<Airport>> {
        val text = query.trim()
        return if (text.isEmpty()) flowOf(emptyList()) else repository.searchAirports(text)
    }
}
