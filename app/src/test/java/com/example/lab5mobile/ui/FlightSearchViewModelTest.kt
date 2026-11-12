package com.example.lab5mobile.ui

import com.example.lab5mobile.MainDispatcherRule
import com.example.lab5mobile.domain.usecase.GetDepartureBoardUseCase
import com.example.lab5mobile.domain.usecase.GetFavoriteRoutesUseCase
import com.example.lab5mobile.domain.usecase.RemoveFavoriteUseCase
import com.example.lab5mobile.domain.usecase.SearchAirportsUseCase
import com.example.lab5mobile.domain.usecase.ToggleFavoriteUseCase
import com.example.lab5mobile.fake.FakeFlightRepository
import com.example.lab5mobile.fake.FakeSearchQueryRepository
import com.example.lab5mobile.fake.TestAirports
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlightSearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeFlightRepository()

    private fun createViewModel(savedQuery: FakeSearchQueryRepository) = FlightSearchViewModel(
        searchAirports = SearchAirportsUseCase(repository),
        getDepartureBoard = GetDepartureBoardUseCase(repository),
        getFavoriteRoutes = GetFavoriteRoutesUseCase(repository),
        toggleFavorite = ToggleFavoriteUseCase(repository),
        removeFavorite = RemoveFavoriteUseCase(repository),
        searchQueryRepository = savedQuery
    )

    /** uiState запускается только при подписчике (WhileSubscribed), поэтому подписываемся в тесте. */
    private fun TestScope.collect(viewModel: FlightSearchViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
    }

    @Test
    fun emptySavedQuery_showsFavorites() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository())
        collect(viewModel)

        assertEquals(ScreenMode.FAVORITES, viewModel.uiState.value.mode)
        assertTrue(viewModel.uiState.value.favorites.isEmpty())
    }

    @Test
    fun savedQuery_restoresFieldAndFlights() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository(initial = "SVO"))
        collect(viewModel)

        val state = viewModel.uiState.value
        assertEquals(ScreenMode.FLIGHTS, state.mode)
        assertEquals("SVO", viewModel.searchQuery.value)
        assertEquals(TestAirports.svo, state.departure)
        assertEquals(TestAirports.all.size - 1, state.flights.size)
    }

    @Test
    fun typing_showsSuggestionsByPassengers() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository())
        collect(viewModel)

        viewModel.onQueryChanged("airport")

        val state = viewModel.uiState.value
        assertEquals(ScreenMode.SUGGESTIONS, state.mode)
        assertEquals(listOf("SVO", "MUC", "DUB", "LIS"), state.airports.map { it.iataCode })
    }

    @Test
    fun unknownText_showsNotFound() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository())
        collect(viewModel)

        viewModel.onQueryChanged("qqq")

        assertEquals(ScreenMode.NOT_FOUND, viewModel.uiState.value.mode)
    }

    @Test
    fun selectingAirport_savesCodeToPreferences() = runTest {
        val preferences = FakeSearchQueryRepository()
        val viewModel = createViewModel(preferences)
        collect(viewModel)

        viewModel.onQueryChanged("mun")
        viewModel.onAirportSelected(TestAirports.muc)

        assertEquals("MUC", preferences.savedValue)
        assertEquals(ScreenMode.FLIGHTS, viewModel.uiState.value.mode)
    }

    @Test
    fun clearingField_forgetsSavedQuery() = runTest {
        val preferences = FakeSearchQueryRepository(initial = "DUB")
        val viewModel = createViewModel(preferences)
        collect(viewModel)

        viewModel.onQueryChanged("")

        assertEquals("", preferences.savedValue)
        assertEquals(ScreenMode.FAVORITES, viewModel.uiState.value.mode)
    }

    @Test
    fun searchAction_picksExactIataCode() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository())
        collect(viewModel)

        viewModel.onQueryChanged("lis")
        viewModel.onSearchSubmitted()

        assertEquals(TestAirports.lis, viewModel.uiState.value.departure)
        assertEquals("LIS", viewModel.searchQuery.value)
    }

    @Test
    fun star_marksFlightAndSecondTapRemovesIt() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository(initial = "SVO"))
        collect(viewModel)
        val flight = viewModel.uiState.value.flights.first { it.destination == TestAirports.lis }

        viewModel.onFlightFavoriteClicked(flight)
        assertTrue(viewModel.uiState.value.flights.first { it.destination == TestAirports.lis }.isFavorite)

        // второе нажатие по той же (устаревшей) карточке всё равно снимает отметку
        viewModel.onFlightFavoriteClicked(flight)
        assertEquals(0, repository.favoriteCount)
    }

    @Test
    fun favoriteRoute_canBeRemovedFromFavoritesList() = runTest {
        val viewModel = createViewModel(FakeSearchQueryRepository())
        collect(viewModel)
        repository.toggleFavorite("MUC", "DUB")

        val route = viewModel.uiState.value.favorites.single()
        viewModel.onFavoriteRouteRemoved(route)

        assertTrue(viewModel.uiState.value.favorites.isEmpty())
    }
}
