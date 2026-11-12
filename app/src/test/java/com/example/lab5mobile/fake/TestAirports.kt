package com.example.lab5mobile.fake

import com.example.lab5mobile.domain.model.Airport

/** Несколько строк из flight_search.db. */
object TestAirports {
    val svo = Airport(7, "SVO", "Sheremetyevo - A.S. Pushkin international airport", 49933000)
    val muc = Airport(28, "MUC", "Munich International Airport", 47959885)
    val dub = Airport(8, "DUB", "Dublin Airport", 32907673)
    val lis = Airport(33, "LIS", "Humberto Delgado Airport", 31173000)

    val all = listOf(svo, muc, dub, lis)
}
