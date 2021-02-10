package com.epam.functions.task1.data

import com.epam.functions.task1.CarConstructor
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay

// This function launches a new bodyLineOne actor
fun CoroutineScope.bodyLineOne(): SendChannel<CarConstructor.PrepareBodyRequest> {
    return actor {

        consumeEach {
            log("work in Body line One")
            delay(200)
            it.sparePartsChannel.send(SpareParts(it.chosenBody))
            it.sparePartsChannel.close()
        }
    }
}

// This function launches a new bodyLineOne actor
fun CoroutineScope.bodyLineTwo(): SendChannel<CarConstructor.PrepareBodyRequest> {
    return actor {

        consumeEach {
            log("work in Body line Two")
            delay(100)
            it.sparePartsChannel.send(SpareParts(it.chosenBody))
            it.sparePartsChannel.close()
        }
    }
}