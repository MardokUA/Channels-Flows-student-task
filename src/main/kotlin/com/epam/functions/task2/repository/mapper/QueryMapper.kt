package com.epam.functions.task2.repository.mapper

import com.epam.functions.task2.api.Asset
import com.epam.functions.task2.repository.Query

class QueryMapper {
    private val regex: Regex = Regex("(\\?\\w{3,4}\$)")

    fun inputToQuery(rawInput: String): Query {
        // try to find type in search request
        val matchResult: MatchResult? = regex.find(rawInput)
        // convert string type to Asset.Type if it found
        val type = matchResult?.value.toAssetType()
        // remove type string value from query if it exist
        val input = type?.run { rawInput.removeRange(matchResult!!.range) } ?: rawInput

        return if (rawInput.startsWith("@")) {
            when (type) {
                null -> Query.RawStartWith(input.drop(1))
                else -> Query.TypedStartWith(input.drop(1), type)
            }
        } else {
            when (type) {
                null -> Query.RawContains(rawInput)
                else -> Query.TypedContains(rawInput, type)
            }
        }
    }

    private fun String?.toAssetType(): Asset.Type? {
        return this?.drop(1)?.capitalize()?.let {
            Asset.Type.valueOf(it)
        }
    }
}