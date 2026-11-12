package com.example.lab5mobile.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    /**
     * Подсказки автозаполнения: код IATA начинается с введённого текста или название его содержит.
     * Шаблоны LIKE готовит репозиторий, спецсимволы % и _ в них экранированы.
     */
    @Query(
        """
        SELECT * FROM airport
        WHERE iata_code LIKE :codePrefix ESCAPE '\' OR name LIKE :namePart ESCAPE '\'
        ORDER BY passengers DESC
        """
    )
    fun searchAirports(codePrefix: String, namePart: String): Flow<List<AirportEntity>>

    @Query("SELECT * FROM airport WHERE iata_code = :code COLLATE NOCASE LIMIT 1")
    fun getAirportByCode(code: String): Flow<AirportEntity?>

    /** Из каждого аэропорта есть рейсы во все остальные, поэтому направления — все аэропорты, кроме вылета. */
    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    fun getDestinations(departureCode: String): Flow<List<AirportEntity>>
}
