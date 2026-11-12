package com.example.lab5mobile.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Последний успешный запрос (код IATA выбранного аэропорта); пустая строка — запроса нет.
 */
interface SearchQueryRepository {

    val lastQuery: Flow<String>

    suspend fun saveLastQuery(query: String)
}
