package com.example.lab5mobile.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.model.Flight
import com.example.lab5mobile.domain.repository.SearchQueryRepository
import com.example.lab5mobile.domain.usecase.GetDepartureBoardUseCase
import com.example.lab5mobile.domain.usecase.GetFavoriteRoutesUseCase
import com.example.lab5mobile.domain.usecase.RemoveFavoriteUseCase
import com.example.lab5mobile.domain.usecase.SearchAirportsUseCase
import com.example.lab5mobile.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModel(
    private val searchAirports: SearchAirportsUseCase,
    private val getDepartureBoard: GetDepartureBoardUseCase,
    private val getFavoriteRoutes: GetFavoriteRoutesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val removeFavorite: RemoveFavoriteUseCase,
    private val searchQueryRepository: SearchQueryRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val selectedAirportCode = MutableStateFlow<String?>(null)
    private val isRestored = MutableStateFlow(false)

    /** Текст поля поиска; экран подставляет его, когда запрос меняет не пользователь. */
    val searchQuery: StateFlow<String> = query.asStateFlow()

    val uiState: StateFlow<FlightSearchUiState> =
        combine(query, selectedAirportCode, isRestored) { text, code, restored ->
            SearchInput(text, code, restored)
        }
            .flatMapLatest { input ->
                when {
                    !input.restored -> flowOf(FlightSearchUiState(mode = ScreenMode.LOADING))
                    input.code != null -> departureBoardState(input.text, input.code)
                    input.text.isBlank() -> favoritesState()
                    else -> suggestionsState(input.text)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = FlightSearchUiState()
            )

    init {
        viewModelScope.launch {
            val savedCode = searchQueryRepository.lastQuery.first()
            if (savedCode.isNotBlank()) {
                query.value = savedCode
                selectedAirportCode.value = savedCode
            }
            isRestored.value = true
        }
    }

    fun onQueryChanged(text: String) {
        if (text == query.value) return
        query.value = text
        val code = selectedAirportCode.value
        if (code != null && !text.trim().equals(code, ignoreCase = true)) {
            selectedAirportCode.value = null
        }
        if (text.isBlank()) {
            saveQuery("")
        }
    }

    fun onAirportSelected(airport: Airport) {
        query.value = airport.iataCode
        selectedAirportCode.value = airport.iataCode
        saveQuery(airport.iataCode)
    }

    /** Кнопка «Найти» на клавиатуре: точное совпадение кода IATA, иначе первая подсказка. */
    fun onSearchSubmitted() {
        val state = uiState.value
        if (state.mode != ScreenMode.SUGGESTIONS || state.airports.isEmpty()) return
        val text = query.value.trim()
        val airport = state.airports.firstOrNull { it.iataCode.equals(text, ignoreCase = true) }
            ?: state.airports.first()
        onAirportSelected(airport)
    }

    fun onFlightFavoriteClicked(flight: Flight) {
        viewModelScope.launch {
            toggleFavorite(flight.departure.iataCode, flight.destination.iataCode)
        }
    }

    fun onFavoriteRouteRemoved(route: FavoriteRoute) {
        viewModelScope.launch {
            removeFavorite(route.departureCode, route.destinationCode)
        }
    }

    private fun departureBoardState(text: String, code: String): Flow<FlightSearchUiState> =
        getDepartureBoard(code)
            .map { board ->
                if (board == null) {
                    FlightSearchUiState(mode = ScreenMode.NOT_FOUND, query = text)
                } else {
                    FlightSearchUiState(
                        mode = ScreenMode.FLIGHTS,
                        query = text,
                        departure = board.departure,
                        flights = board.flights
                    )
                }
            }
            .withErrorState(text)

    private fun favoritesState(): Flow<FlightSearchUiState> =
        getFavoriteRoutes()
            .map { routes -> FlightSearchUiState(mode = ScreenMode.FAVORITES, favorites = routes) }
            .withErrorState("")

    private fun suggestionsState(text: String): Flow<FlightSearchUiState> =
        searchAirports(text)
            .map { airports ->
                if (airports.isEmpty()) {
                    FlightSearchUiState(mode = ScreenMode.NOT_FOUND, query = text)
                } else {
                    FlightSearchUiState(mode = ScreenMode.SUGGESTIONS, query = text, airports = airports)
                }
            }
            .withErrorState(text)

    private fun Flow<FlightSearchUiState>.withErrorState(text: String): Flow<FlightSearchUiState> =
        catch { error ->
            Log.e(TAG, "Database query failed", error)
            emit(FlightSearchUiState(mode = ScreenMode.ERROR, query = text))
        }

    private fun saveQuery(value: String) {
        viewModelScope.launch {
            searchQueryRepository.saveLastQuery(value)
        }
    }

    private companion object {
        const val TAG = "FlightSearchViewModel"
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
