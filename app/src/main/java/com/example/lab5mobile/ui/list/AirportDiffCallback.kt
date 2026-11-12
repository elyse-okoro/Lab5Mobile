package com.example.lab5mobile.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.example.lab5mobile.domain.model.Airport

class AirportDiffCallback : DiffUtil.ItemCallback<Airport>() {

    override fun areItemsTheSame(oldItem: Airport, newItem: Airport): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Airport, newItem: Airport): Boolean =
        oldItem == newItem
}
