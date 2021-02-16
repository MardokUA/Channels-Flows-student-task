package com.epam.functions.task1

import com.epam.functions.task1.data.BodyParts
import com.epam.functions.task1.data.EquipmentParts
import com.epam.functions.task1.factory.ChosenBody
import com.epam.functions.task1.factory.ChosenEquipment
import com.epam.functions.task1.utils.*
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
    // has to implement select<BodyParts> and log(combineBodyBodyLine1) inside onSend() for bodyLineOne and log(combineBodyBodyLine2) inside onSend() for bodyLineTwo!!! it is important for test
    bodyLineOne.onSend(req) {
        log(combineBodyBodyLine1)
        channel.receive()
    }
    bodyLineTwo.onSend(req) {
        log(combineBodyBodyLine2)
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
    // has to implement select<BodyParts> and log(combineEquipmentEquipmentLine1) inside onSend() for equipmentLineOne and log(combineEquipmentEquipmentLine2) inside onSend() for equipmentLineTwo!!! it is important for test
    equipmentLineOne.onSend(req) {
        log(combineEquipmentEquipmentLine1)
        channel.receive()
    }
    equipmentLineTwo.onSend(req) {
        log(combineEquipmentEquipmentLine2)
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
    // should shotDown all channels and check is it off
    bodyLineOne.close()
    bodyLineTwo.close()
    equipmentLineOne.close()
    equipmentLineTwo.close()
    return equipmentLineOne.isClosedForSend and
            equipmentLineTwo.isClosedForSend and
            bodyLineOne.isClosedForSend and
            bodyLineTwo.isClosedForSend
}
