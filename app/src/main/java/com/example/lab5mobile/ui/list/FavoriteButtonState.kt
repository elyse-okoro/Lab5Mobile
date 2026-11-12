package com.example.lab5mobile.ui.list

import com.example.lab5mobile.R
import com.google.android.material.button.MaterialButton

/**
 * Звёздочка в карточке: закрашенная — маршрут в избранном, контурная — нет.
 */
fun MaterialButton.showFavoriteState(isFavorite: Boolean) {
    setIconResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_outline)
    contentDescription = context.getString(
        if (isFavorite) R.string.action_remove_favorite else R.string.action_add_favorite
    )
}
