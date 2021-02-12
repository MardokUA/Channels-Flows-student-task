package com.epam.functions.task1

import com.epam.functions.task1.data.Part
import com.epam.functions.task1.data.SpareParts
import com.epam.functions.task1.data.createBodyLine
import com.epam.functions.task1.data.equipmentLine
import com.epam.functions.task1.factory.ChosenBody
import com.epam.functions.task1.factory.CompiledEquipment
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.select

class CarConstructor constructor(
    scope: CoroutineScope,
    val bodyLineOne: SendChannel<PrepareBodyRequest>,
    val bodyLineTwo: SendChannel<PrepareBodyRequest>,
    val equipmentLineOne: SendChannel<CombineEquipmentRequest>,
    val equipmentLineTwo: SendChannel<CombineEquipmentRequest>
) : CoroutineScope by scope {

    data class PrepareBodyRequest(
        val chosenBody: ChosenBody,
        val sparePartsChannel: SendChannel<SpareParts>
    )

    data class CombineEquipmentRequest(
        val equipment: Part.Equipment,
        val equipmentChannel: SendChannel<CompiledEquipment>
    )

    suspend fun combineBody(chosenBody: ChosenBody) = select<SpareParts> {
        val channel = Channel<SpareParts>()
        val req = PrepareBodyRequest(chosenBody, channel)
        bodyLineOne.onSend(req) {
            log("combineBody bodyLineOne")
            channel.receive()
        }
        bodyLineTwo.onSend(req) {
            log("combineBody bodyLineTwo")
            channel.receive()
        }
    }

    suspend fun combineEquipment(equipment: Part.Equipment) = select<CompiledEquipment> {
        val channel = Channel<CompiledEquipment>()
        val req = CombineEquipmentRequest(equipment, channel)
        equipmentLineOne.onSend(req) {
            log("combineEquipment equipmentLineOne")
            channel.receive()
        }
        equipmentLineTwo.onSend(req) {
            log("combineEquipment equipmentLineTwo")
            channel.receive()
        }
    }

    fun shutdown(): Boolean {
        bodyLineOne.close()
        bodyLineTwo.close()
        equipmentLineOne.close()
        equipmentLineTwo.close()
        return equipmentLineOne.isClosedForSend and
                equipmentLineTwo.isClosedForSend and
                bodyLineOne.isClosedForSend and
                bodyLineTwo.isClosedForSend
    }

    companion object {
        fun createInstance(scope: CoroutineScope): CarConstructor {
            return CarConstructor(
                scope = scope,
                bodyLineOne = scope.createBodyLine("Body line 1"),
                bodyLineTwo = scope.createBodyLine("Body line 2"),
                equipmentLineOne = scope.equipmentLine("Equipment line 1"),
                equipmentLineTwo = scope.equipmentLine("Equipment line 2")
            )
        }
    }
}