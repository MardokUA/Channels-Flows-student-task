package com.epam.functions.task2.api

import com.epam.functions.task2.api.factory.CastFactory
import com.epam.functions.task2.api.factory.MovieFactory
import com.epam.functions.task2.api.factory.TvChannelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

@kotlinx.coroutines.ExperimentalCoroutinesApi
class SearchDataSource(
    private val movieFactory: MovieFactory,
    private val tvChannelFactory: TvChannelFactory,
    private val castFactory: CastFactory
) : SearchApi {

    override suspend fun searchByContains(query: String): Flow<Asset> {
        return mergeAll()
            .delayOnEach()
            .filter { it.getPoster().contains(query, true) }
    }

    override suspend fun searchByContains(query: String, type: Asset.Type): Flow<Asset> {
        return type.toFlow()
            .delayOnEach()
            .filter {
                it.getPoster().contains(query, true)
            }
    }

    override suspend fun searchByStartWith(query: String): Flow<Asset> {
        return mergeAll()
            .delayOnEach()
            .filter { it.getPoster().startsWith(query, true) }
    }

    override suspend fun searchByStartWith(query: String, type: Asset.Type): Flow<Asset> {
        return type.toFlow()
            .delayOnEach()
            .filter { it.getPoster().startsWith(query, true) }
    }

    private fun Asset.Type.toFlow(): Flow<Asset> {
        return when (this) {
            Asset.Type.VOD -> movieFactory.provideContent()
            Asset.Type.LIVE -> tvChannelFactory.provideContent()
            Asset.Type.CREW -> castFactory.provideContent()
        }
    }

    private fun mergeAll() = merge(
        movieFactory.provideContent(),
        tvChannelFactory.provideContent(),
        castFactory.provideContent()
    )

    private fun <T : Asset> Flow<T>.delayOnEach() = onEach { delay(FAKE_DELAY) }

    companion object {
        private const val FAKE_DELAY = 100L
    }
}