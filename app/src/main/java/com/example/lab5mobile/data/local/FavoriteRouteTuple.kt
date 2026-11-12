package com.example.lab5mobile.data.local

import androidx.room.ColumnInfo

/**
 * Результат запроса избранного вместе с названиями аэропортов (LEFT JOIN с airport).
 */
data class FavoriteRouteTuple(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "departure_code")
    val departureCode: String,
    @ColumnInfo(name = "departure_name")
    val departureName: String?,
    @ColumnInfo(name = "destination_code")
    val destinationCode: String,
    @ColumnInfo(name = "destination_name")
    val destinationName: String?
)
