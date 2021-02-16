package com.epam.functions.task2

import com.epam.functions.task2.api.Asset
import com.epam.functions.task2.repository.Query
import com.epam.functions.task2.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * In Clean Architecture this class should be named as UseCase or Interactot.
 * All necessary logic should be implemented here.
 */
/*
TODO: implement all business logic which converts input stream to [Query]
 * your realization should use it's own CoroutineScope.
 * your realization should receive CoroutineDispatcher in constructor
  (it gives possibility to write tests)
 */
class SearchEngine(
    private val repository: SearchRepository,
    dispatcher: CoroutineDispatcher
) {
    private val scope = CoroutineScope(dispatcher)
    private val regex: Regex = Regex("(\\?\\w{3,4}\$)")

    fun searchContentAsync(rawInput: String): Deferred<List<Asset>> = scope.async {
        // try to find type in search request
        val matchResult: MatchResult? = regex.find(rawInput)
        // convert string type to Asset.Type if it found
        val type = matchResult?.value.toAssetType()
        // remove type string value from query if it exist
        val input = type?.run { rawInput.removeRange(matchResult!!.range) } ?: rawInput
        // convert input to query based on type
        val query = input.toQuery(type)

        repository.searchContentAsync(query)
    }

    private fun String?.toAssetType(): Asset.Type? {
        return this?.drop(1)?.capitalize()?.let {
            Asset.Type.valueOf(it)
        }
    }

    private fun String.toQuery(type: Asset.Type?): Query {
        return if (startsWith("@")) {
            when (type) {
                null -> Query.RawStartWith(drop(1))
                else -> Query.TypedStartWith(drop(1), type)
            }
        } else {
            when (type) {
                null -> Query.RawContains(this)
                else -> Query.TypedContains(this, type)
            }
        }
    }
}