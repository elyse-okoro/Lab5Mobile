package com.example.lab5mobile.data

import com.example.lab5mobile.domain.repository.FlightRepository
import com.example.lab5mobile.domain.repository.SearchQueryRepository

/**
 * Контейнер зависимостей уровня приложения (ручное внедрение зависимостей).
 */
interface AppContainer {
    val flightRepository: FlightRepository
    val searchQueryRepository: SearchQueryRepository
}
