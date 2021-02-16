package com.epam.functions.task2

import com.epam.functions.task2.api.Asset
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
internal class SearchEngineTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    private val engine = DependencyProvider.provideEngine(testDispatcher)

    @Test
    fun `input 'in' (contains) should return all types of assets`() = testScope.runBlockingTest {
        val testQuery = "in"
        val result = engine.searchContentAsync(testQuery).await()
        val type = result.map { it.type }
        type.shouldContainAll(listOf(Asset.Type.VOD, Asset.Type.LIVE, Asset.Type.CREW))
    }

    @Test
    fun `input 'as' (startWith) should return empty list`() = testScope.runBlockingTest {
        val testQuery = "@as"
        val result = engine.searchContentAsync(testQuery).await()
        result.shouldBeEmpty()
    }

    @Test
    fun `input 'se' (contains, type VOD) should return 2 movies`() = testScope.runBlockingTest {
        val testQuery = "se?VOD"
        val result = engine.searchContentAsync(testQuery).await()
        assertTrue("Only VOD assets should be bound") {
            result.map { it.type }.all { it == Asset.Type.VOD }
        }
    }

    @Test
    fun `input 'an' (contains, type LIVE) should return 2 tv channels`() = testScope.runBlockingTest {
        val testQuery = "an?LIVE"
        val result = engine.searchContentAsync(testQuery).await()
        assertTrue("Two live content assets should be found") {
            result.map { it.type }.all { it == Asset.Type.LIVE }
        }
    }

    @Test
    fun `input 'an' (startWith, type LIVE) should return only 1 tv channels`() = testScope.runBlockingTest {
        val testQuery = "@an?LIVE"
        val result = engine.searchContentAsync(testQuery).await()
        assertTrue("Only ONE LIVE content item should be found") {
            result.size == 1 && result.first().type == Asset.Type.LIVE
        }
    }
}