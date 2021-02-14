package com.epam.functions.task2.engine

import com.epam.functions.task2.content.Asset

data class Query(
    val type: Asset.Type?,
    val input: String,
    val predicate: (String, String) -> Boolean
)