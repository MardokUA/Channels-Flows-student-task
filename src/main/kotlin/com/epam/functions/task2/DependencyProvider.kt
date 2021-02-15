package com.epam.functions.task2

import com.epam.functions.task2.api.SearchApi
import com.epam.functions.task2.api.SearchDataSource
import com.epam.functions.task2.api.factory.CastFactory
import com.epam.functions.task2.api.factory.MovieFactory
import com.epam.functions.task2.api.factory.TvChannelFactory
import com.epam.functions.task2.repository.ContentDataSource
import com.epam.functions.task2.repository.SearchRepository
import com.epam.functions.task2.repository.mapper.QueryMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
object DependencyProvider {

    fun provideRepository(dispatcher: CoroutineDispatcher): SearchRepository {
        return ContentDataSource(
            searchApi = api,
            mapper = queryMapper,
            dispatcher = dispatcher
        )
    }

    val queryMapper
        get() = QueryMapper()

    val api: SearchApi
        get() = SearchDataSource(
            movieFactory = MovieFactory(),
            tvChannelFactory = TvChannelFactory(),
            castFactory = CastFactory()
        )
}