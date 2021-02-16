package com.epam.functions.task1.factory

import com.epam.functions.task1.PrepareBodyRequest
import com.epam.functions.task1.PrepareEquipmentRequest
import com.epam.functions.task1.combineBody
import com.epam.functions.task1.combineEquipment
import com.epam.functions.task1.data.*
import com.epam.functions.task1.utils.log
import com.epam.functions.task1.utils.name
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay


// producer of completed car orders
// TODO: remove method impl and add documentation
@ExperimentalCoroutinesApi
fun createCar(
    orders: ReceiveChannel<Car>,
    scope: CoroutineScope,
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
): ReceiveChannel<OutPut.FinishedCar> =
    scope.produce(CoroutineName(name)) {
        for (order in orders) {
            log("Processing order: $order")
            val preparedBody = prepareBody(order.body())
            val preparedEquipment = preparedEquipment(order.equipment())
            val body = getBody(preparedBody, bodyLineOne, bodyLineTwo)
            val equipment = getEquipment(preparedEquipment, equipmentLineOne, equipmentLineTwo)
            val finalCompose = finalCompose(order, body, equipment)
            send(finalCompose)
        }
    }

suspend fun getEquipment(
    preparedEquipment: ChosenEquipment,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
) = combineEquipment(preparedEquipment, equipmentLineOne, equipmentLineTwo)


suspend fun getBody(
    preparedBody: ChosenBody,
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>
) = combineBody(preparedBody, bodyLineOne, bodyLineTwo)


suspend fun prepareBody(body: Part.Body): ChosenBody {
    log("Preparing car body $body")
    delay(400)
    return ChosenBody(body)
}

suspend fun preparedEquipment(equipment: Part.Equipment): ChosenEquipment {
    log("Preparing car equipment $equipment")
    delay(400)
    return ChosenEquipment(equipment)
}

// composes provided BodyParts and EquipmentParts to FinishedCar
suspend fun finalCompose(
    order: Car,
    bodyParts: BodyParts,
    equipment: EquipmentParts
): OutPut.FinishedCar {
    log("Combining parts $bodyParts, $equipment")
    delay(100)
    return OutPut.FinishedCar(order, bodyParts, equipment)
}