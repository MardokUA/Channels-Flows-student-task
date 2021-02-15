package com.epam.functions.task2.api.factory

import com.epam.functions.task2.api.Cast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class CastFactory : ContentFactory<Cast>() {

    override val dataList: Array<String> = arrayOf(
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
        return List(dataList.size) { index ->
            Cast(
                name = dataList[index].split(" ").first(),
                surname = dataList[index].split(" ").last(),
                filmCount = index + 1
            )
        }.asFlow()
    }
}