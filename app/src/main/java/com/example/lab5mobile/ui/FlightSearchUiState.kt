package com.example.lab5mobile.ui

import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.model.Flight

/**
 * Что показывает экран: подсказки, рейсы из выбранного аэропорта, избранное или пустое состояние.
 */
data class FlightSearchUiState(
    val mode: ScreenMode = ScreenMode.LOADING,
    val query: String = "",
    val airports: List<Airport> = emptyList(),
    val departure: Airport? = null,
    val flights: List<Flight> = emptyList(),
    val favorites: List<FavoriteRoute> = emptyList()
)
