package com.epam.functions.task2.factory

import com.epam.functions.task2.content.Cast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow


class CastFactory : ContentFactory<Cast>() {

    override val nameList: Array<String> = arrayOf(
        "Adriana Ferdynand",
        "Walenty Kuba",
        "Jarek Franciszka",
        "Quintella Hayley",
        "Fraser Starr",
        "Wallis Chuck",
        "Nino Avksenti",
        "Daviti Ketevan",
        "Ioane Korneli",
        "Mariami Nika",
    )

    override fun provideContent(): Flow<Cast> {
        return List(nameList.size) { index ->
            Cast(
                name = nameList[index].split(" ").first(),
                surname = nameList[index].split(" ").last(),
                filmCount = index + 1
            )
        }.asFlow()
    }
}