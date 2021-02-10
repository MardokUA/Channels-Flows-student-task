package com.epam.functions

import com.epam.functions.data.Part
import com.epam.functions.data.SpareParts
import com.epam.functions.factory.ChosenBody
import com.epam.functions.factory.CompiledEquipment
import com.epam.functions.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select

class CarConstructor(scope: CoroutineScope) : CoroutineScope by scope {

    data class PrepareBodyRequest(
        val chosenBody: ChosenBody,
        val sparePartsChannel: SendChannel<SpareParts>
    )

    data class CombineEquipmentRequest(
        val equipment: Part.Equipment,
        val equipmentChannel: SendChannel<CompiledEquipment>
    )

    private val bodyLineOne: SendChannel<PrepareBodyRequest> = actor {
        consumeEach {
            log("work in Body line One")
            delay(200)
            it.sparePartsChannel.send(SpareParts(it.chosenBody))
            it.sparePartsChannel.close()
        }
    }

    private val bodyLineTwo: SendChannel<PrepareBodyRequest> = actor {
        consumeEach {
            log("work in Body line Two")
            delay(100)
            it.sparePartsChannel.send(SpareParts(it.chosenBody))
            it.sparePartsChannel.close()
        }
    }

    private val equipmentLineOne: SendChannel<CombineEquipmentRequest> = actor {
        consumeEach {
            log("work in Equipment Line One")
            delay(200)
            it.equipmentChannel.send(CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }

    private val equipmentLineTwo: SendChannel<CombineEquipmentRequest> = actor {
        consumeEach {
            log("work in Equipment Line Two")
            delay(100)
            it.equipmentChannel.send(CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }

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