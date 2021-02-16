package com.epam.functions.task2.repository

import com.epam.functions.task2.api.Asset
import com.epam.functions.task2.api.SearchApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toCollection

@ExperimentalCoroutinesApi
class ContentDataSource(private val searchApi: SearchApi) : SearchRepository {

    override suspend fun searchContentAsync(query: Query): List<Asset> {
        val request = when (query) {
            is Query.RawContains -> searchApi.searchByContains(query.input)
            is Query.RawStartWith -> searchApi.searchByStartWith(query.input)
            is Query.TypedContains -> searchApi.searchByContains(query.input, query.type)
            is Query.TypedStartWith -> searchApi.searchByStartWith(query.input, query.type)
        }
        return request.toCollection(mutableListOf())
    }
}