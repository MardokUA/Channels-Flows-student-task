package com.epam.functions.task1.data

import com.epam.functions.task1.CarConstructor
import com.epam.functions.task1.factory.CompiledEquipment
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlin.random.Random

// This function launches a new equipmentLineOne actor
fun CoroutineScope.equipmentLine(name: String): SendChannel<CarConstructor.CombineEquipmentRequest> {
    return actor {
        consumeEach {
            log("work in $name")
            delay(Random.nextLong(100, 500))
            it.equipmentChannel.send(CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }
}
