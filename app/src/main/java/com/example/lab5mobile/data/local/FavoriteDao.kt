package com.example.lab5mobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query(
        """
        SELECT favorite.id AS id,
               favorite.departure_code AS departure_code,
               departure.name AS departure_name,
               favorite.destination_code AS destination_code,
               destination.name AS destination_name
        FROM favorite
        LEFT JOIN airport AS departure ON departure.iata_code = favorite.departure_code
        LEFT JOIN airport AS destination ON destination.iata_code = favorite.destination_code
        ORDER BY favorite.id DESC
        """
    )
    fun getFavoriteRoutes(): Flow<List<FavoriteRouteTuple>>

    @Query("SELECT destination_code FROM favorite WHERE departure_code = :departureCode")
    fun getFavoriteDestinationCodes(departureCode: String): Flow<List<String>>

    @Query(
        "SELECT COUNT(*) FROM favorite " +
            "WHERE departure_code = :departureCode AND destination_code = :destinationCode"
    )
    suspend fun countRoute(departureCode: String, destinationCode: String): Int

    @Insert
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteRoute(departureCode: String, destinationCode: String)
}
