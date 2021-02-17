package com.epam.functions.task2

import com.epam.functions.task2.api.SearchApi
import com.epam.functions.task2.api.SearchDataSource
import com.epam.functions.task2.api.factory.CastFactory
import com.epam.functions.task2.api.factory.MovieFactory
import com.epam.functions.task2.api.factory.TvChannelFactory
import com.epam.functions.task2.engine.SearchEngine
import com.epam.functions.task2.repository.ContentDataSource
import com.epam.functions.task2.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This is a simple realization of Service Locator pattern.
 * It uses 'fabric method' pattern to provide all classes
 * you need in one place.
 */
//TODO: add your realization of each contract in this task
@ExperimentalCoroutinesApi
object DependencyProvider {

    fun provideEngine(dispatcher: CoroutineDispatcher): SearchEngine {
        return SearchEngine(
            repository = provideRepository(),
            dispatcher = dispatcher
        )
    }

    private fun provideRepository(): SearchRepository {
        return ContentDataSource(searchApi = provideApi())
    }

    private fun provideApi(): SearchApi = SearchDataSource(
        movieFactory = MovieFactory(),
        tvChannelFactory = TvChannelFactory(),
        castFactory = CastFactory()
    )
}