package teacher.com.epam.task1.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import teacher.com.epam.task1.PrepareEquipmentRequest
import teacher.com.epam.task1.utils.log
import kotlin.random.Random

/*
TODO should return actor and contain log("work in equipmentLineName") for each equipmentLine it is important for test!!!
  it is recommended to use delay inside actor body
 */
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
