package com.epam.functions.task2

import com.epam.functions.task2.api.Asset
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SearchRepositoryTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    private val repository = DependencyProvider.provideRepository(testDispatcher)

    @Test
    fun `input 'in' (contains) should return all types of assets`() = testScope.runBlockingTest {
        val testQuery = "in"
        val result = repository.searchContentAsync(testQuery).await()
        val type = result.map { it.type }
        type.shouldContainAll(listOf(Asset.Type.VOD, Asset.Type.LIVE, Asset.Type.CREW))
    }

    @Test
    fun `input 'as' (startWith) should return empty list`() = testScope.runBlockingTest {
        val testQuery = "@as"
        val result = repository.searchContentAsync(testQuery).await()
        result.shouldBeEmpty()
    }

    @Test
    fun `input 'se' (contains, type VOD) should return 2 movies`() = testScope.runBlockingTest {
        val testQuery = "se?VOD"
        val result = repository.searchContentAsync(testQuery).await()
        assert(result.map { it.type }.all { it == Asset.Type.VOD })
    }

    @Test
    fun `input 'an' (contains, type LIVE) should return 2 tv channels`() = testScope.runBlockingTest {
        val testQuery = "an?LIVE"
        val result = repository.searchContentAsync(testQuery).await()
        assert(result.map { it.type }.all { it == Asset.Type.LIVE })
    }

    @Test
    fun `input 'an' (startWith, type LIVE) should return only 1 tv channels`() = testScope.runBlockingTest {
        val testQuery = "@an?LIVE"
        val result = repository.searchContentAsync(testQuery).await()
        assert(result.size == 1 && result.first().type == Asset.Type.LIVE)
    }
}