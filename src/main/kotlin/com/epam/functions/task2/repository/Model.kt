package com.epam.functions.task2.repository

import com.epam.functions.task2.api.Asset
import com.epam.functions.task2.api.SearchApi

/**
 * Represents concrete type of search request to cover
 * all search cases in [SearchApi]
 */
/*
TODO: add all necessary subclasses to satisfy [SearchRepository] contract
      and cover all [SearchApi] cases.
 */
sealed class Query(val input: String) {
    class TypedContains(input: String, val type: Asset.Type) : Query(input)
    class TypedStartWith(input: String, val type: Asset.Type) : Query(input)
    class RawContains(input: String) : Query(input)
    class RawStartWith(input: String) : Query(input)
}
