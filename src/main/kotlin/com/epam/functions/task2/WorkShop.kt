package com.epam.functions.task2

import com.epam.functions.task2.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

private const val TITLE_BORDER = "============="
var isRunning = true

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {

    val repository = DependencyProvider.provideRepository(Dispatchers.IO)

    println(
        "$TITLE_BORDER " + "Welcome to EPAM Online TV $TITLE_BORDER" +
                "\nThere is a lot of content could be found there." +
                "\nAll you need is to type your search request or \"exit\" to end program"
    )
    while (isRunning) {
        print("TYPE REQUEST: ")
        val query = readLine().orEmpty()
        when {
            query.isBlank() -> println("Incorrect input")
            query.isEndProgram() -> proceedExit()
            else -> proceedSearch(query, repository)
        }
    }
}

@ExperimentalCoroutinesApi
private suspend fun proceedSearch(query: String, repository: SearchRepository) {
    println("Start searching...")
    val result = runCatching { repository.searchContentAsync(query.trim()).await() }.getOrNull()
    when {
        result == null -> println("Invalid asset type is request")
        result.isEmpty() -> println("Sorry, but we found nothing")
        else -> {
            println("Result is:")
            result.forEach { println(it.getPoster()) }
        }
    }
}

private fun proceedExit() {
    isRunning = false
    println("Thank yot for choosing out service. See you next time")
}

private fun String.isEndProgram(): Boolean = this.equals("exit", ignoreCase = true)