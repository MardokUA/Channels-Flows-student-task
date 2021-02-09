package com.epam.functions

import com.epam.functions.data.Part
import com.epam.functions.data.SpareParts
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
        val chosenBody: Part.Body.ChosenBody,
        val sparePartsChannel: SendChannel<SpareParts>
    )

    data class CombineEquipmentRequest(
        val equipment: Part.Equipment,
        val equipmentChannel: SendChannel<Part.CompiledEquipment>
    )

    private val bodyLineOne: SendChannel<PrepareBodyRequest> = actor {
        consumeEach {
            log("work in Body line One")
            delay(20)
            it.sparePartsChannel.send(SpareParts(it.chosenBody))
            it.sparePartsChannel.close()
        }
    }

    private val bodyLineTwo: SendChannel<PrepareBodyRequest> = actor {
        consumeEach {
            log("work in Body line Two")
            delay(20)
            it.sparePartsChannel.send(SpareParts(it.chosenBody))
            it.sparePartsChannel.close()
        }
    }

    private val equipmentLineOne: SendChannel<CombineEquipmentRequest> = actor {
        consumeEach {
            log("work in Equipment Line One")
            delay(10)
            it.equipmentChannel.send(Part.CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }

    private val equipmentLineTwo: SendChannel<CombineEquipmentRequest> = actor {
        consumeEach {
            log("work in Equipment Line Two")
            delay(10)
            it.equipmentChannel.send(Part.CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }

    suspend fun combineBody(chosenBody: Part.Body.ChosenBody) = select<SpareParts> {
        val channel = Channel<SpareParts>()
        val req = PrepareBodyRequest(chosenBody, channel)
        bodyLineOne.onSend(req) {
            channel.receive()
        }
        bodyLineTwo.onSend(req) {
            channel.receive()
        }
    }

    suspend fun combineEquipment(equipment: Part.Equipment) = select<Part.CompiledEquipment> {
        val channel = Channel<Part.CompiledEquipment>()
        val req = CombineEquipmentRequest(equipment, channel)
        equipmentLineOne.onSend(req) {
            channel.receive()
        }
        equipmentLineTwo.onSend(req) {
            channel.receive()
        }
    }

    fun shutdown() {
        bodyLineOne.close()
        bodyLineTwo.close()
        equipmentLineOne.close()
        equipmentLineTwo.close()
    }
}