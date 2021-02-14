package com.epam.functions.task2

import com.epam.functions.task2.content.Asset
import com.epam.functions.task2.engine.Query
import com.epam.functions.task2.engine.SearchEngine
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toCollection

// ve ->
@ExperimentalCoroutinesApi
class SearchFacade(private val searchEngine: SearchEngine) {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val regex: Regex = Regex("(\\?\\w{3,4}\$)")

    suspend fun searchContentAsync(rawInput: String): Deferred<List<Asset>> {
        // try to find type in search request
        val matchResult: MatchResult? = regex.find(rawInput)
        // convert string type to Asset.Type if it found
        val type = matchResult?.value.toAssetType()
        // remove type string value from query if it exist
        val input = type?.run { rawInput.removeRange(matchResult!!.range) } ?: rawInput
        // define predicate, which will be applied to asset's poster
        val textPredicate: (String, String) -> Boolean = defineSearchApproach(rawInput)

        return scope.async {
            searchEngine.search(
                Query(
                    type = type,
                    predicate = textPredicate,
                    input = input.removePrefix("@")
                )
            ).toCollection(mutableListOf())
        }
    }

    private fun defineSearchApproach(rawInput: String) = if (rawInput.startsWith("@")) {
        { assetName: String, input: String -> assetName.startsWith(input, true) }
    } else {
        { assetName: String, input: String -> assetName.contains(input, true) }
    }

    private fun String?.toAssetType(): Asset.Type? {
        return this?.drop(1)?.capitalize()?.let {
            Asset.Type.valueOf(it)
        }
    }
}