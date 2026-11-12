package com.example.lab5mobile.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.example.lab5mobile.R
import com.example.lab5mobile.databinding.ItemFlightBinding
import com.example.lab5mobile.domain.model.Flight

class FlightViewHolder(
    private val binding: ItemFlightBinding,
    onFavoriteClick: (Flight) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var flight: Flight? = null

    init {
        binding.favoriteButton.setOnClickListener { flight?.let(onFavoriteClick) }
    }

    fun bind(item: Flight) {
        flight = item
        binding.departCode.text = item.departure.iataCode
        binding.departName.text = item.departure.name
        binding.arriveCode.text = item.destination.iataCode
        binding.arriveName.text = item.destination.name
        binding.flightNumber.text = binding.root.context.getString(R.string.flight_number, item.number)
        binding.favoriteButton.showFavoriteState(item.isFavorite)
    }
}
