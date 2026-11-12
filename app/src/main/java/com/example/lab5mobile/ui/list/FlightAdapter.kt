package com.example.lab5mobile.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.lab5mobile.databinding.ItemFlightBinding
import com.example.lab5mobile.domain.model.Flight

/**
 * Рейсы из выбранного аэропорта со звёздочкой избранного.
 */
class FlightAdapter(
    private val onFavoriteClick: (Flight) -> Unit
) : ListAdapter<Flight, FlightViewHolder>(FlightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding = ItemFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
