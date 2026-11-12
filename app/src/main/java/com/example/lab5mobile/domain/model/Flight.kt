package com.example.lab5mobile.domain.model

/**
 * Рейс между двумя аэропортами. Расписания в базе нет, поэтому номер рейса
 * складывается из идентификаторов аэропортов и одинаков при каждом запуске.
 */
data class Flight(
    val departure: Airport,
    val destination: Airport,
    val isFavorite: Boolean
) {
    val number: Int
        get() = departure.id * 100 + destination.id
}
