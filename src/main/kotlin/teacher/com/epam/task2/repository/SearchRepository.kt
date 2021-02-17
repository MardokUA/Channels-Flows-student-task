package teacher.com.epam.task2.repository

import kotlinx.coroutines.flow.Flow
import teacher.com.epam.task2.api.Asset

/**
 * Represent data layer, which designed to use with user's raw input.
 * All inputs should be converted to [Query]
 */
/*
TODO: add realization, which will interact with SearchApi and returns the result.
 * your realization should hold reference to [SearchApi]
*/
interface SearchRepository {

    suspend fun searchContentAsync(query: Query): Flow<Asset>
}