package com.epam.functions.task2

import com.epam.functions.task2.factory.CastFactory
import com.epam.functions.task2.factory.MovieFactory
import com.epam.functions.task2.factory.TvChannelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.runBlocking

private const val TITLE_BORDER = "============="

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {
    val engine = SearchEngine(
        movieFactory = MovieFactory(),
        tvChannelFactory = TvChannelFactory(),
        castFactory = CastFactory()
    )
    var isRunning = true

    println(" $TITLE_BORDER Welcome to EPAM Online TV $TITLE_BORDER\nThere are a lot of content could be found there ")

    while (isRunning) {
        print("Please, type your search query or EXIT for quite: ")
        val query = readLine().orEmpty()
        when {
            query.isBlank() -> {
                println("Incorrect input")
                continue
            }
            query == ("EXIT") -> isRunning = false
            else -> {
                engine.search(query)
                    .onStart { println("There is your result:") }
                    .collect { println(it.getPoster()) }
            }
        }
    }
}