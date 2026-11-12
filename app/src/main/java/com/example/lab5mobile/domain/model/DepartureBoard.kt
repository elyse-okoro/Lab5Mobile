package com.example.lab5mobile.domain.model

/**
 * Аэропорт вылета и все рейсы из него с отметкой избранного.
 */
data class DepartureBoard(
    val departure: Airport,
    val flights: List<Flight>
)
