package com.example.lab5mobile.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.example.lab5mobile.R
import com.example.lab5mobile.databinding.ItemAirportBinding
import com.example.lab5mobile.domain.model.Airport
import java.text.NumberFormat

class AirportViewHolder(
    private val binding: ItemAirportBinding,
    onAirportClick: (Airport) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var airport: Airport? = null

    init {
        binding.root.setOnClickListener { airport?.let(onAirportClick) }
    }

    fun bind(item: Airport) {
        airport = item
        val resources = binding.root.resources
        val passengers = NumberFormat.getIntegerInstance().format(item.passengers)
        binding.airportCode.text = item.iataCode
        binding.airportName.text = item.name
        binding.airportPassengers.text = resources.getQuantityString(
            R.plurals.passengers_per_year,
            item.passengers,
            passengers
        )
    }
}
