package com.epam.functions.task2.repository

import com.epam.functions.task2.api.Asset
import com.epam.functions.task2.api.SearchApi
import com.epam.functions.task2.repository.mapper.QueryMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toCollection

@ExperimentalCoroutinesApi
class ContentDataSource(
    private val searchApi: SearchApi,
    private val mapper: QueryMapper
) : SearchRepository {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun searchContentAsync(rawInput: String): Deferred<List<Asset>> = scope.async {
        val request = when (val query = mapper.inputToQuery(rawInput)) {
            is Query.RawContains -> searchApi.searchByContains(query.input)
            is Query.RawStartWith -> searchApi.searchByStartWith(query.input)
            is Query.TypedContains -> searchApi.searchByContains(query.input, query.type)
            is Query.TypedStartWith -> searchApi.searchByStartWith(query.input, query.type)
        }
        request.toCollection(mutableListOf())
    }
}