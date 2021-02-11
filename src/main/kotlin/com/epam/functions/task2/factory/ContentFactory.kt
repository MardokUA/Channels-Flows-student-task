package com.epam.functions.task2.factory

import com.epam.functions.task2.content.Asset
import kotlinx.coroutines.flow.Flow

abstract class ContentFactory<out T : Asset> {

    protected abstract val nameList: Array<String>

    abstract fun provideContent(): Flow<T>
}