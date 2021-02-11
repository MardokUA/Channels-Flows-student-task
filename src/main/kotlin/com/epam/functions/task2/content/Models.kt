package com.epam.functions.task2.content

import java.text.SimpleDateFormat
import java.util.*

abstract class Asset {

    enum class Type {
        VOD,
        LIVE,
        CREW
    }

    abstract fun getTitle(): String
    abstract val type: Type
    abstract fun getPoster(): String

    override fun toString(): String = getPoster()
}

data class Movie(
    val label: String,
    val releaseYear: Date
) : Asset() {
    override fun getTitle(): String = label
    override val type: Type get() = Type.VOD
    override fun getPoster(): String = "$label (${SimpleDateFormat("dd.MM.yyyy").format(releaseYear)})"
}

data class TvChannel(
    val label: String,
    val number: Int
) : Asset() {
    override fun getTitle(): String = label
    override val type: Type get() = Type.LIVE
    override fun getPoster(): String = "№$number $label"
}

data class Cast(
    val name: String,
    val surname: String,
    val filmCount: Int
) : Asset() {
    override fun getTitle(): String = "$name $surname"
    override val type: Type get() = Type.CREW
    override fun getPoster(): String = "${getTitle()} ($filmCount films)"
}