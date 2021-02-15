package com.epam.functions.task2.api.factory

import com.epam.functions.task2.api.Asset
import com.epam.functions.task2.api.Cast
import com.epam.functions.task2.api.Movie
import com.epam.functions.task2.api.TvChannel
import kotlinx.coroutines.flow.Flow

/** Represents base content factory, which provides concrete type of [Asset]s */
/*
TODO:
 * add three factories with given content below:
    - with Asset.Type.VOD content (Movies data)
    - with Asset.Type.LIVE content
    - with Asset.Type.CAST content (Cast data)
 IMPORTANT: do not modify data because it uses in tests.
 */
abstract class ContentFactory<out T : Asset> {

    /** Represents movie, live or cast data */
    protected abstract val dataList: Array<*>

    /** Provides flow with concrete content: [Movie], [TvChannel] or [Cast] */
    abstract fun provideContent(): Flow<T>
}

/*
    Movies data:
        | name                                 | release year in unix time
        "Harry Potter and the Sorcerer's Stone"| 1005861600000
        "28 Weeks Later"                       | 1178830800000
        "Beowulf"                              | 1195596000000
        "The Seven Deadly Sins"                | 1416088800000
        "Die Hard"                             | 585345600000
        "Rocky"                                | 217371600000
        "Doctor Strange"                       | 1477342800000
        "Braveheart"                           | 801262800000
        "Beauty and the Beast"                 | 1487800800000
        "Seven"                                | 811717200000

   Live data:
        "1+1"
        "Football 1"
        "Inter"
        "STB"
        "5 channel"
        "ICTV"
        "National Geographic"
        "Animal Planet"
        "Ukraine HD"
        "History HD"

   Cast data:
      "Adriana Ferdynand"
      "Walenty Kuba"
      "Jarek Franciszka"
      "Quintella Hayley"
      "Fraser Starr"
      "Wallis Chuck"
      "Nino Avksenti"
      "Daviti Ketevan"
      "Ioane Korneli"
      "Mariami Nika"
 */