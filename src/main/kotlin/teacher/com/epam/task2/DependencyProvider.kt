package teacher.com.epam.task2

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import teacher.com.epam.task2.api.SearchApi
import teacher.com.epam.task2.api.SearchDataSource
import teacher.com.epam.task2.api.factory.CastFactory
import teacher.com.epam.task2.api.factory.MovieFactory
import teacher.com.epam.task2.api.factory.TvChannelFactory
import teacher.com.epam.task2.engine.SearchEngine
import teacher.com.epam.task2.repository.ContentDataSource
import teacher.com.epam.task2.repository.SearchRepository

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