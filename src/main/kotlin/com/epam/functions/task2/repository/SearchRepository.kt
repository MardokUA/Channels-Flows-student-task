package com.epam.functions.task2.repository

import com.epam.functions.task2.api.Asset
import kotlinx.coroutines.Deferred

/**
 * Represent data layer, which designed to use with user's raw input.
 * All inputs should be converted to [Query]
 */
//TODO:
// * add realization, which will interact with SearchApi and returns the result.
// * your realization should hold reference to [SearchApi] and [QueryMapper].
// * your realization should use it's own CoroutineScope.
// * your realization should receive CoroutineDispatcher in constructor.
interface SearchRepository {

    suspend fun searchContentAsync(rawInput: String): Deferred<List<Asset>>
}