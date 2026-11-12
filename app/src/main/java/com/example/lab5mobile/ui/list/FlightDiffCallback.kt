package com.example.lab5mobile.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.example.lab5mobile.domain.model.Flight

class FlightDiffCallback : DiffUtil.ItemCallback<Flight>() {

    override fun areItemsTheSame(oldItem: Flight, newItem: Flight): Boolean =
        oldItem.departure.id == newItem.departure.id &&
            oldItem.destination.id == newItem.destination.id

    override fun areContentsTheSame(oldItem: Flight, newItem: Flight): Boolean =
        oldItem == newItem
}
