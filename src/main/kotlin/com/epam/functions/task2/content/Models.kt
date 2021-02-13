package com.epam.functions.task2.content

import java.text.SimpleDateFormat
import java.util.*

/**
 * Represents base class of content on the our Online TV service.
 */
abstract class Asset {

    enum class Type {

        /**
         * Video on demand (movies, serials, trailers e.t.c). This is just a simple video file,
         * which are stored on the server and can be converted to the video stream.
         * */
        VOD,

        /**
         * Video content, which are currently streaming ont the server (tv channels, podcasts, e.t.c).
         */
        LIVE,

        /**
         * All people, who are participated in video making process (actors, director, operator e.t.c).
         */
        CREW
    }

    abstract val type: Type

    /**
     * Title of the asset, which holds all neccessary information
     * TIP: should be used in [com.epam.functions.task2.engine.SearchEngine] to match search query.
     * */
    abstract fun getTitle(): String
    abstract fun getPoster(): String
    override fun toString(): String = getPoster()
}

/** VOD example */
data class Movie(
    val label: String,
    val releaseYear: Date,
    override val type: Type = Type.VOD
) : Asset() {
    override fun getTitle(): String = label
    override fun getPoster(): String = "$label (${SimpleDateFormat("dd.MM.yyyy").format(releaseYear)})"
}

/** Live example */
data class TvChannel(
    val label: String,
    val number: Int,
    override val type: Type = Type.LIVE
) : Asset() {
    override fun getTitle(): String = label
    override fun getPoster(): String = "№$number $label"
}

/** Crew example */
data class Cast(
    val name: String,
    val surname: String,
    val filmCount: Int,
    override val type: Type = Type.CREW
) : Asset() {
    override fun getTitle(): String = "$name $surname"
    override fun getPoster(): String = "${getTitle()} ($filmCount films)"
}