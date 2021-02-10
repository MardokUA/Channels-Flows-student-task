package com.epam.functions.task1

import com.epam.functions.task1.data.Part
import com.epam.functions.task1.data.SpareParts
import com.epam.functions.task1.factory.ChosenBody
import com.epam.functions.task1.factory.CompiledEquipment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.select

class CarConstructor(
    scope: CoroutineScope,
    var bodyLineOne: SendChannel<PrepareBodyRequest>,
    var bodyLineTwo: SendChannel<PrepareBodyRequest>,
    var equipmentLineOne: SendChannel<CombineEquipmentRequest>,
    var equipmentLineTwo: SendChannel<CombineEquipmentRequest>
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
            channel.receive()
        }
        bodyLineTwo.onSend(req) {
            channel.receive()
        }
    }

    suspend fun combineEquipment(equipment: Part.Equipment) = select<CompiledEquipment> {
        val channel = Channel<CompiledEquipment>()
        val req = CombineEquipmentRequest(equipment, channel)
        equipmentLineOne.onSend(req) {
            channel.receive()
        }
        equipmentLineTwo.onSend(req) {
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
}