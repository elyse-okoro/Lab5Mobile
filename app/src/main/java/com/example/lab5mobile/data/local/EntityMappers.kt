package com.example.lab5mobile.data.local

import com.example.lab5mobile.domain.model.Airport
import com.example.lab5mobile.domain.model.FavoriteRoute

fun AirportEntity.toDomain(): Airport = Airport(
    id = id,
    iataCode = iataCode,
    name = name,
    passengers = passengers
)

fun FavoriteRouteTuple.toDomain(): FavoriteRoute = FavoriteRoute(
    id = id,
    departureCode = departureCode,
    departureName = departureName,
    destinationCode = destinationCode,
    destinationName = destinationName
)
