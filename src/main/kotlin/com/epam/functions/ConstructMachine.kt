package com.epam.functions

import com.epam.functions.data.Body
import com.epam.functions.data.Equipment
import com.epam.functions.data.SpareParts
import com.epam.functions.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.*

class ConstructMachine(scope: CoroutineScope): CoroutineScope by scope {
    data class PrepareBodyRequest(val choosedBody: Body.ChoosedBody, val sparePartsChannel: SendChannel<SpareParts>)

    data class CombineEquipmentRequest(val equipment: Equipment, val equipmentChannel: SendChannel<Equipment.CompiledEquipment>)

    private val bodyLineOne: SendChannel<PrepareBodyRequest> = actor() {
        consumeEach {
            log("work in Body line One")
            delay(20)
            it.sparePartsChannel.send(SpareParts(it.choosedBody))
            it.sparePartsChannel.close()
        }
    }

    private val bodyLineTwo: SendChannel<PrepareBodyRequest> = actor() {
        consumeEach {
            log("work in Body line Two")
            delay(20)
            it.sparePartsChannel.send(SpareParts(it.choosedBody))
            it.sparePartsChannel.close()
        }
    }

    private val equipmentLineOne: SendChannel<CombineEquipmentRequest> = actor() {
        consumeEach {
            log("work in Equipment Line One")
            delay(10)
            it.equipmentChannel.send(Equipment.CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }

    private val equipmentLineTwo: SendChannel<CombineEquipmentRequest> = actor() {
        consumeEach {
            log("work in Equipment Line Two")
            delay(10)
            it.equipmentChannel.send(Equipment.CompiledEquipment(it.equipment))
            it.equipmentChannel.close()
        }
    }

    suspend fun combineBody(choosedBody: Body.ChoosedBody) = select<SpareParts> {
        val channel = Channel<SpareParts>()
        val req = PrepareBodyRequest(choosedBody, channel)
        bodyLineOne.onSend(req) {
            channel.receive()
        }
        bodyLineTwo.onSend(req) {
            channel.receive()
        }
    }

    suspend fun combineEquipment(equipment: Equipment) = select<Equipment.CompiledEquipment> {
        val channel = Channel<Equipment.CompiledEquipment>()
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