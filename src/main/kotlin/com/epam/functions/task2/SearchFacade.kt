package com.epam.functions.task2

import com.epam.functions.task2.content.Asset
import com.epam.functions.task2.engine.SearchEngine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toCollection

@ExperimentalCoroutinesApi
class SearchFacade(private val searchEngine: SearchEngine) {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val regex: Regex = Regex("(\\?\\w{3,4}\$)")

    suspend fun searchContentAsync(rawQuery: String): Deferred<List<Asset>> {
        // try to find type in search request
        val matchResult: MatchResult? = regex.find(rawQuery)
        // convert string type to Asset.Type if it found
        val type = matchResult?.value?.drop(1)?.let { Asset.Type.valueOf(it) }
        // remove type string value from query if it exist
        val query = type?.run { rawQuery.removeRange(matchResult.range) } ?: rawQuery

        return scope.async {
            val searchRequest = if (type != null) {
                searchEngine.search(query, type)
            } else {
                searchEngine.search(query)
            }
            searchRequest.toCollection(mutableListOf())
        }
    }
}