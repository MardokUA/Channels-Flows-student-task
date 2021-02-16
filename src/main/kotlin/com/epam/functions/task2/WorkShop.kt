package com.epam.functions.task2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

private const val TITLE_BORDER = "============="
var isRunning = true

/*
TODO: write a program, which should read user's input and shows the result.
      Main logic is described in Readme.md. There are some additional requirements:
    * your realization should use [DependencyProvider] to obtain objects.
    * when program is started, user should see a greetings message
    * when program is started, user should see a tip "how to exit program"
    * when program is completed, user should see a farewell message
 */
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {

    val engine = DependencyProvider.provideEngine(Dispatchers.IO)

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
            else -> proceedSearch(query, engine)
        }
    }
}

@ExperimentalCoroutinesApi
private suspend fun proceedSearch(query: String, repository: SearchEngine) {
    println("Start searching...")
    val result = runCatching { repository.searchContentAsync(query.trim()).await() }.getOrNull()
    when {
        result == null -> println("Invalid asset type in request")
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