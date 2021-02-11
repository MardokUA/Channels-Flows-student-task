package com.epam.functions.task2

import com.epam.functions.task2.content.Asset
import com.epam.functions.task2.factory.CastFactory
import com.epam.functions.task2.factory.MovieFactory
import com.epam.functions.task2.factory.TvChannelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.merge

@kotlinx.coroutines.ExperimentalCoroutinesApi
class SearchEngine(
    private val movieFactory: MovieFactory,
    private val tvChannelFactory: TvChannelFactory,
    private val castFactory: CastFactory
) {

    fun search(name: String): Flow<Asset> {
        return merge(
            movieFactory.provideContent(),
            tvChannelFactory.provideContent(),
            castFactory.provideContent()
        ).filter { it.getTitle().toLowerCase().contains(name) }
    }

    fun search(name: String, type: Asset.Type): Flow<Asset> {
        return merge(
            movieFactory.provideContent(),
            tvChannelFactory.provideContent(),
            castFactory.provideContent()
        ).filter { it.type == type && it.getTitle().contains(name) }
    }
}