package com.epam.functions.task2.engine

import com.epam.functions.task2.content.Asset
import com.epam.functions.task2.factory.CastFactory
import com.epam.functions.task2.factory.MovieFactory
import com.epam.functions.task2.factory.TvChannelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

@kotlinx.coroutines.ExperimentalCoroutinesApi
class ContentSearchEngine(
    private val movieFactory: MovieFactory,
    private val tvChannelFactory: TvChannelFactory,
    private val castFactory: CastFactory
) : SearchEngine {

    override suspend fun search(query: String): Flow<Asset> {
        return mergeAll().filter { it.getPoster().contains(query, true) }
    }

    override suspend fun search(query: String, type: Asset.Type): Flow<Asset> {
        return mergeAll().filter { it.type == type && it.getPoster().contains(query) }
    }

    private fun mergeAll() = merge(
        movieFactory.provideContent(),
        tvChannelFactory.provideContent(),
        castFactory.provideContent()
    ).onEach { delay(100) }
}