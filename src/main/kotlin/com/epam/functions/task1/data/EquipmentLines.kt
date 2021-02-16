package com.epam.functions.task1.data

import com.epam.functions.task1.PrepareEquipmentRequest
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlin.random.Random

// This function launches a new equipmentLineOne actor
fun CoroutineScope.createEquipmentLine(equipmentLineName: String): SendChannel<PrepareEquipmentRequest> {
    // should return actor and contain log("work in equipmentLineName") for each equipmentLine it is important for test!!!
    return actor {
        consumeEach {
            log("work in $equipmentLineName")
            delay(Random.nextLong(100, 500))
            it.equipmentChannel.send(EquipmentParts(it.equipment))
        }
    }
}

// implementation that is exactly expected
// fun CoroutineScope.createEquipmentLine(equipmentLineName: String): SendChannel<PrepareEquipmentRequest> {
//    return ... {
//        ... {
//            log("work in equipmentLineName")
//            delay(Random.nextLong(100, 500))
//            ...
//        }
//    }
//}
// Please replace ... with your implementation