package teacher.com.epam.task2.api.factory

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import teacher.com.epam.task2.api.TvChannel

class TvChannelFactory : ContentFactory<TvChannel>() {

    override val dataList: Array<String> = arrayOf(
        "1+1",
        "Football 1",
        "Inter",
        "STB",
        "5 channel",
        "ICTV",
        "National Geographic",
        "Animal Planet",
        "Ukraine HD",
        "History HD",
    )

    override fun provideContent(): Flow<TvChannel> {
        return List(dataList.size) { index ->
            TvChannel(
                label = dataList[index],
                number = index + 1
            )
        }.asFlow()
    }
}