package com.epam.functions.task1.data

import com.epam.functions.task1.PrepareBodyRequest
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlin.random.Random

// This function launches a new bodyLineOne actor
fun CoroutineScope.createBodyLine(name: String): SendChannel<PrepareBodyRequest> {
    return actor {
        consumeEach {
            log("work in $name")
            delay(Random.nextLong(100, 500))
            it.bodyPartsChannel.send(BodyParts(it.chosenBody))
        }
    }
}
