package com.example.lab5mobile.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Строка таблицы airport из предзаполненной базы flight_search.db.
 */
@Entity(tableName = "airport")
data class AirportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "iata_code")
    val iataCode: String,
    @ColumnInfo(name = "passengers")
    val passengers: Int
)
