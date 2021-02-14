package com.epam.functions.task2

import com.epam.functions.task2.engine.ContentSearchEngine
import com.epam.functions.task2.factory.CastFactory
import com.epam.functions.task2.factory.MovieFactory
import com.epam.functions.task2.factory.TvChannelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

private const val TITLE_BORDER = "============="

private fun String.isEndProgram(): Boolean = this.equals("exit", ignoreCase = true)

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {

    var isRunning = true

    val engine = ContentSearchEngine(
        movieFactory = MovieFactory(),
        tvChannelFactory = TvChannelFactory(),
        castFactory = CastFactory()
    )

    val searcher = SearchFacade(engine)

    println(" $TITLE_BORDER Welcome to EPAM Online TV $TITLE_BORDER\nThere is a lot of content could be found there.")
    while (isRunning) {
        print("Please, type your search query or EXIT for quite: ")
        val query = readLine().orEmpty()
        when {
            query.isBlank() -> {
                println("Incorrect input")
                continue
            }
            query.isEndProgram() -> isRunning = false
            else -> proceedSearch(searcher, query)
        }
    }
}

@ExperimentalCoroutinesApi
private suspend fun proceedSearch(searcher: SearchFacade, query: String) {
    println("Starting search...")
    val result = runCatching { searcher.searchContentAsync(query.trim()).await() }.getOrNull()
    when {
        result == null -> println("Invalid assert type")
        result.isEmpty() -> println("Sorry, but we found nothing")
        else -> {
            println("Result is:")
            result.forEach { println(it.getPoster()) }
        }
    }
}