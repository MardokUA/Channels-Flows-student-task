package com.epam.functions.task2.engine

import com.epam.functions.task2.content.Asset
import kotlinx.coroutines.flow.Flow

interface SearchEngine {

    suspend fun search(query: Query): Flow<Asset>
}