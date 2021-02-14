package com.epam.functions.task1

import com.epam.functions.task1.data.BodyParts
import com.epam.functions.task1.data.EquipmentParts
import com.epam.functions.task1.factory.ChosenBody
import com.epam.functions.task1.factory.ChosenEquipment
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.select


data class PrepareBodyRequest(
    val chosenBody: ChosenBody,
    val bodyPartsChannel: SendChannel<BodyParts>
)

data class PrepareEquipmentRequest(
    val equipment: ChosenEquipment,
    val equipmentChannel: SendChannel<EquipmentParts>
)


// suspend function to combine body parts. It has selector that defines which channel is currently available to send
// the main thing is our channels have buffer = 1 it means that only one request could be processed in a time.
// Once the request is sent to the channel, we wait for a response and deliver the result. The bodyLine
// implementation sends the result on the provided channel and then closes the channel.
suspend fun combineBody(
    chosenBody: ChosenBody,
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>
) = select<BodyParts> {
    val channel = Channel<BodyParts>()
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
// suspend function to combine equipment parts. It has selector that defines which channel is currently available to send
// the main thing is our channels have buffer = 1 it means that only one request could be processed in a time.
// Once the request is sent to the channel, we wait for a response and deliver the result. The equipmentLine
// implementation sends the result on the provided channel and then closes the channel.
suspend fun combineEquipment(
    equipment: ChosenEquipment,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
) = select<EquipmentParts> {
    val channel = Channel<EquipmentParts>()
    val req = PrepareEquipmentRequest(equipment, channel)
    equipmentLineOne.onSend(req) {
        log("combineEquipment equipmentLineOne")
        channel.receive()
    }
    equipmentLineTwo.onSend(req) {
        log("combineEquipment equipmentLineTwo")
        channel.receive()
    }
}

// closes all channel, checks are they closed and bring the result
@ExperimentalCoroutinesApi
fun shutdown(
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
): Boolean {
    bodyLineOne.close()
    bodyLineTwo.close()
    equipmentLineOne.close()
    equipmentLineTwo.close()
    return equipmentLineOne.isClosedForSend and
            equipmentLineTwo.isClosedForSend and
            bodyLineOne.isClosedForSend and
            bodyLineTwo.isClosedForSend
}
