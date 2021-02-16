package com.epam.functions.task2.engine

import com.epam.functions.task2.api.Asset

data class SearchResult(
    val assets: List<Asset>,
    val type: Asset.Type,
    val groupName: String
)