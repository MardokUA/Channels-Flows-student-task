package com.epam.functions.task2.factory

import com.epam.functions.task2.content.TvChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class TvChannelFactory : ContentFactory<TvChannel>() {

    override val nameList: Array<String> = arrayOf(
        "1+1",
        "Inter",
        "Football 1",
        "Zik",
        "5 channel",
        "ICTV",
        "National Geographic",
        "Animal Planet",
        "STB",
        "History HD",
    )

    override fun provideContent(): Flow<TvChannel> {
        return List(nameList.size) { index ->
            TvChannel(
                label = nameList[index],
                number = index + 1
            )
        }.asFlow()
    }
}