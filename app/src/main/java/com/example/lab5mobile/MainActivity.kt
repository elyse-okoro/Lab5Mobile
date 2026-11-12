package com.example.lab5mobile

import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab5mobile.databinding.ActivityMainBinding
import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.ui.FlightSearchUiState
import com.example.lab5mobile.ui.FlightSearchViewModel
import com.example.lab5mobile.ui.FlightSearchViewModelFactory
import com.example.lab5mobile.ui.ScreenMode
import com.example.lab5mobile.ui.list.AirportAdapter
import com.example.lab5mobile.ui.list.FavoriteRouteAdapter
import com.example.lab5mobile.ui.list.FlightAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: FlightSearchViewModel by viewModels {
        FlightSearchViewModelFactory((application as FlightSearchApplication).container)
    }

    private val airportAdapter = AirportAdapter { airport -> onAirportClicked(airport) }
    private val flightAdapter = FlightAdapter { flight -> viewModel.onFlightFavoriteClicked(flight) }
    private val favoriteRouteAdapter = FavoriteRouteAdapter { route -> viewModel.onFavoriteRouteRemoved(route) }

    private val layoutManager by lazy { GridLayoutManager(this, 1) }
    private var listBottomPadding = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listBottomPadding = binding.resultsList.paddingBottom
        applyWindowInsets()
        setupSearchField()
        binding.resultsList.layoutManager = layoutManager

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.searchQuery.collect { query -> syncSearchField(query) } }
                launch { viewModel.uiState.collect { state -> render(state) } }
                launch { viewModel.saveFailed.collect { showSaveError() } }
            }
        }
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { _, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            binding.appBar.updatePadding(top = bars.top)
            binding.main.updatePadding(left = bars.left, right = bars.right)
            binding.resultsList.updatePadding(bottom = listBottomPadding + maxOf(bars.bottom, keyboard.bottom))
            insets
        }
    }

    private fun showSaveError() {
        Snackbar.make(binding.main, R.string.error_save, Snackbar.LENGTH_LONG).show()
    }

    private fun setupSearchField() {
        binding.searchInput.doAfterTextChanged { text ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
        }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onSearchSubmitted()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun onAirportClicked(airport: Airport) {
        viewModel.onAirportSelected(airport)
        hideKeyboard()
    }

    private fun syncSearchField(query: String) {
        val field = binding.searchInput
        if (field.text?.toString() == query) return
        field.setText(query)
        field.setSelection(query.length)
    }

    private fun render(state: FlightSearchUiState) {
        binding.progress.isVisible = state.mode == ScreenMode.LOADING
        when (state.mode) {
            ScreenMode.LOADING -> showNothing()
            ScreenMode.SUGGESTIONS -> {
                showList(airportAdapter, getString(R.string.title_suggestions))
                airportAdapter.submitList(state.airports)
            }
            ScreenMode.FLIGHTS -> {
                val departure = state.departure ?: return
                showList(
                    flightAdapter,
                    getString(R.string.title_flights_from, departure.iataCode, departure.name)
                )
                flightAdapter.submitList(state.flights)
            }
            ScreenMode.FAVORITES -> {
                if (state.favorites.isEmpty()) {
                    showMessage(
                        title = getString(R.string.title_favorites),
                        iconRes = R.drawable.ic_star_outline,
                        message = getString(R.string.empty_favorites)
                    )
                } else {
                    showList(favoriteRouteAdapter, getString(R.string.title_favorites))
                    favoriteRouteAdapter.submitList(state.favorites)
                }
            }
            ScreenMode.NOT_FOUND -> showMessage(
                title = null,
                iconRes = R.drawable.ic_travel_explore,
                message = getString(R.string.empty_search, state.query.trim())
            )
            ScreenMode.ERROR -> showMessage(
                title = null,
                iconRes = R.drawable.ic_error_outline,
                message = getString(R.string.error_database)
            )
        }
    }

    private fun showList(adapter: RecyclerView.Adapter<*>, title: String) {
        if (binding.resultsList.adapter !== adapter) {
            // подсказки — простой список, карточки рейсов на широком экране — в две колонки
            layoutManager.spanCount =
                if (adapter === airportAdapter) 1 else resources.getInteger(R.integer.results_span_count)
            binding.resultsList.adapter = adapter
        }
        binding.sectionTitle.text = title
        binding.sectionTitle.isVisible = true
        binding.resultsList.isVisible = true
        binding.emptyState.isVisible = false
    }

    private fun showMessage(title: String?, @DrawableRes iconRes: Int, message: String) {
        binding.sectionTitle.text = title.orEmpty()
        binding.sectionTitle.isVisible = title != null
        binding.resultsList.isVisible = false
        binding.emptyIcon.setImageResource(iconRes)
        binding.emptyText.text = message
        binding.emptyState.isVisible = true
    }

    private fun showNothing() {
        binding.sectionTitle.isVisible = false
        binding.resultsList.isVisible = false
        binding.emptyState.isVisible = false
    }

    private fun hideKeyboard() {
        binding.searchInput.clearFocus()
        WindowCompat.getInsetsController(window, binding.searchInput)
            .hide(WindowInsetsCompat.Type.ime())
    }
}
