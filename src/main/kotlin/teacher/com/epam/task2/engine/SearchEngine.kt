package teacher.com.epam.task2.engine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import teacher.com.epam.task2.api.Asset
import teacher.com.epam.task2.repository.Query
import teacher.com.epam.task2.repository.SearchRepository

/**
 * In Clean Architecture this class should be named as UseCase or Interactot.
 * All necessary logic should be implemented here.
 */
/*
TODO: implement all business logic which converts input stream to [Query]
 * your realization should use it's own CoroutineScope.
 * your realization should receive CoroutineDispatcher in constructor
   (it gives possibility to write tests)
 * flow should executed on provided CoroutineDispatcher
 */
class SearchEngine(
    private val repository: SearchRepository,
    private val dispatcher: CoroutineDispatcher
) {
    private val regex: Regex = Regex("(\\?\\w{3,4}\$)")

    suspend fun searchContentAsync(rawInput: String): Flow<SearchResult> {
        // validate rawInput and decide is proceed needed
        validateInput(rawInput) { message -> error(message) }
        // try to find type in search request
        val matchResult: MatchResult? = regex.find(rawInput)
        // convert string type to Asset.Type if it found
        val type = matchResult?.value.toAssetType()
        // remove type string value from query if it exist
        val input = type?.run { rawInput.removeRange(matchResult!!.range) } ?: rawInput
        // convert input to query based on type
        val query = input.toQuery(type)

        return repository.searchContentAsync(query)
            .flowOn(dispatcher)
            .toList(mutableListOf())
            .groupBy { it.type }
            .map { data -> SearchResult(data.value, data.key, data.key.toGroupName()) }
            .asFlow()

    }

    private inline fun validateInput(rawInput: String, block: (String) -> Nothing) {
        val message = when {
            rawInput == START_WITH_INDICATOR -> "Incorrect input"
            rawInput.isEmpty() -> "Input is empty"
            rawInput.isBlank() -> "Input is blank"
            else -> null
        }
        message?.let(block)
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

    private fun Asset.Type.toGroupName(): String {
        return when (this) {
            Asset.Type.VOD -> "-- Movies --"
            Asset.Type.LIVE -> "-- TvChannels --"
            Asset.Type.CREW -> "-- Cast and Crew --"
        }
    }

    companion object {
        private const val START_WITH_INDICATOR = "@"
    }
}

