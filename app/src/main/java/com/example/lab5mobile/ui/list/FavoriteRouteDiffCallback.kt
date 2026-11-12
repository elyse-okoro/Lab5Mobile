package com.example.lab5mobile.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.example.lab5mobile.domain.model.FavoriteRoute

class FavoriteRouteDiffCallback : DiffUtil.ItemCallback<FavoriteRoute>() {

    override fun areItemsTheSame(oldItem: FavoriteRoute, newItem: FavoriteRoute): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FavoriteRoute, newItem: FavoriteRoute): Boolean =
        oldItem == newItem
}
