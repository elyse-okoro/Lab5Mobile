package com.example.lab5mobile.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.lab5mobile.databinding.ItemFlightBinding
import com.example.lab5mobile.domain.model.FavoriteRoute

class FavoriteRouteAdapter(
    private val onRemoveClick: (FavoriteRoute) -> Unit
) : ListAdapter<FavoriteRoute, FavoriteRouteViewHolder>(FavoriteRouteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRouteViewHolder {
        val binding = ItemFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteRouteViewHolder(binding, onRemoveClick)
    }

    override fun onBindViewHolder(holder: FavoriteRouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
