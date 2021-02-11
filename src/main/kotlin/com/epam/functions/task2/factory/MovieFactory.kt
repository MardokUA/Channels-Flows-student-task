package com.epam.functions.task2.factory

import com.epam.functions.task2.content.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.*
import kotlin.random.Random

class MovieFactory : ContentFactory<Movie>(){

    override val nameList: Array<String> = arrayOf(
        "The Seven Samurai",
        "Bonnie and Clyde",
        "Reservoir Dogs",
        "Airplane!",
        "Doctor Zhivago",
        "Rocky",
        "Memento",
        "Braveheart",
        "Beauty and the Beast",
        "Seven",
    )

    override fun provideContent(): Flow<Movie> {
        return List(nameList.size) { index ->
            Movie(
                label = nameList[index],
                releaseYear = Date(createRandomYear())
            )
        }.asFlow()
    }

    private fun createRandomYear() = Random.nextLong(YEAR_1976, YEAR_2020)

    companion object {
        private const val YEAR_1976 = 186526800000L
        private const val YEAR_2020 = 1575064800000L
    }
}