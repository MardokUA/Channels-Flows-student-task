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
fun CoroutineScope.createBodyLine(bodyLineName: String): SendChannel<PrepareBodyRequest> {
    // should return actor and contain log("work in bodyLineName") for each bodyLine it is important for test!!!
    return actor {
        consumeEach {
            log("work in $bodyLineName")
            delay(Random.nextLong(100, 500))
            it.bodyPartsChannel.send(BodyParts(it.chosenBody))
        }
    }
}


// implementation that is exactly expected
//fun CoroutineScope.createBodyLine(bodyLineName: String): SendChannel<PrepareBodyRequest> {
//    return ... {
//        ... {
//            log("work in $bodyLineName")
//            delay(Random.nextLong(100, 500))
//            ...
//        }
//    }
//}
// Please replace ... with your implementation