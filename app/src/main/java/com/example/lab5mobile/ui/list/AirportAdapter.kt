package com.example.lab5mobile.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.lab5mobile.databinding.ItemAirportBinding
import com.example.lab5mobile.domain.model.Airport

/**
 * Подсказки автозаполнения: аэропорты, подходящие под введённый текст.
 */
class AirportAdapter(
    private val onAirportClick: (Airport) -> Unit
) : ListAdapter<Airport, AirportViewHolder>(AirportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder {
        val binding = ItemAirportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AirportViewHolder(binding, onAirportClick)
    }

    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
