package com.example.lab5mobile.domain.model

/**
 * Избранный маршрут. Названия могут отсутствовать, если кода уже нет в таблице airport.
 */
data class FavoriteRoute(
    val id: Int,
    val departureCode: String,
    val departureName: String?,
    val destinationCode: String,
    val destinationName: String?
)
