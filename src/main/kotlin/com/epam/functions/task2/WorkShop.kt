package com.epam.functions.task2

import com.epam.functions.task2.engine.SearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

var isRunning = true

/*
TODO: write a program, which should read user's input and shows the result.
      Main logic is described in Readme.md. There are some additional requirements:
    * your realization should use [DependencyProvider] to obtain objects.
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {

    val engine = DependencyProvider.provideEngine(Dispatchers.IO)

    println(
        "$TITLE_BORDER $GREETINGS_MESSAGE $TITLE_BORDER $HINT_MESSAGE"
    )
    while (isRunning) {
        print("TYPE REQUEST: ")
        val query = readLine().orEmpty()
        when {
            query.isBlank() -> println("Incorrect input")
            query.isEndProgram() -> proceedExit()
            else -> proceedSearch(query, engine)
        }
    }
}

@ExperimentalCoroutinesApi
private suspend fun proceedSearch(query: String, repository: SearchEngine) {
    println("Start searching...")
    val searchResult = runCatching {
        repository.searchContentAsync(query.trim()).toList(mutableListOf())
    }.getOrNull()
    when {
        searchResult == null -> println("Invalid asset type in request")
        searchResult.isEmpty() -> println("Sorry, but we found nothing")
        else -> {
            println("Result is:")
            searchResult.forEach { result ->
                println(result.groupName)
                result.assets.forEach { println(it.toString()) }
            }
        }
    }
}

private fun proceedExit() {
    isRunning = false
    println("Thank yot for choosing out service. See you next time")
}

private fun String.isEndProgram(): Boolean = this.equals("exit", ignoreCase = true)