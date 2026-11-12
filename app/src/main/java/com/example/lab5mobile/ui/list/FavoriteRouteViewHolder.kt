package com.example.lab5mobile.ui.list

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.lab5mobile.databinding.ItemFlightBinding
import com.example.lab5mobile.domain.model.FavoriteRoute

/**
 * Карточка избранного маршрута. Разметка та же, что у рейса, но номера рейса нет,
 * а звёздочка всегда закрашена и убирает маршрут из избранного.
 */
class FavoriteRouteViewHolder(
    private val binding: ItemFlightBinding,
    onRemoveClick: (FavoriteRoute) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var route: FavoriteRoute? = null

    init {
        binding.favoriteButton.setOnClickListener { route?.let(onRemoveClick) }
        binding.flightNumber.isVisible = false
    }

    fun bind(item: FavoriteRoute) {
        route = item
        binding.departCode.text = item.departureCode
        binding.departName.text = item.departureName.orEmpty()
        binding.departName.isVisible = item.departureName != null
        binding.arriveCode.text = item.destinationCode
        binding.arriveName.text = item.destinationName.orEmpty()
        binding.arriveName.isVisible = item.destinationName != null
        binding.favoriteButton.showFavoriteState(isFavorite = true)
    }
}
