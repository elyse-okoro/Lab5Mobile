package com.example.lab5mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab5mobile.data.AppContainer
import com.example.lab5mobile.domain.usecase.GetDepartureBoardUseCase
import com.example.lab5mobile.domain.usecase.GetFavoriteRoutesUseCase
import com.example.lab5mobile.domain.usecase.SearchAirportsUseCase
import com.example.lab5mobile.domain.usecase.ToggleFavoriteUseCase

/**
 * У ViewModel есть параметры конструктора, поэтому без своей фабрики её не создать.
 */
class FlightSearchViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightSearchViewModel::class.java)) {
            val repository = container.flightRepository
            @Suppress("UNCHECKED_CAST")
            return FlightSearchViewModel(
                searchAirports = SearchAirportsUseCase(repository),
                getDepartureBoard = GetDepartureBoardUseCase(repository),
                getFavoriteRoutes = GetFavoriteRoutesUseCase(repository),
                toggleFavorite = ToggleFavoriteUseCase(repository),
                searchQueryRepository = container.searchQueryRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
