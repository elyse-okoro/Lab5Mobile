package com.example.lab5mobile.fake

import com.example.lab5mobile.domain.repository.SearchQueryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSearchQueryRepository(initial: String = "") : SearchQueryRepository {

    private val stored = MutableStateFlow(initial)

    val savedValue: String
        get() = stored.value

    override val lastQuery: Flow<String> = stored

    override suspend fun saveLastQuery(query: String) {
        stored.value = query
    }
}
