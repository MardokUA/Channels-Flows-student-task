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
fun CoroutineScope.createEquipmentLine(name: String): SendChannel<PrepareEquipmentRequest> {
    return actor {
        consumeEach {
            log("work in $name")
            delay(Random.nextLong(100, 500))
            it.equipmentChannel.send(EquipmentParts(it.equipment))
        }
    }
}
