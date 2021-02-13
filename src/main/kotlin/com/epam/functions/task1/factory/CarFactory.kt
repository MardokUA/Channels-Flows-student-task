package com.epam.functions.task1.factory

import com.epam.functions.task1.PrepareBodyRequest
import com.epam.functions.task1.PrepareEquipmentRequest
import com.epam.functions.task1.combineBody
import com.epam.functions.task1.combineEquipment
import com.epam.functions.task1.data.*
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

const val NAME = "Car factory"

// producer of completed car orders
// TODO: remove method impl and add documentation
fun createCar(
    orders: ReceiveChannel<Car>, scope: CoroutineScope,
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
): ReceiveChannel<OutPut.FinishedCar> =
    scope.produce(CoroutineName(NAME)) {
        for (order in orders) {
            log("Processing order: $order")
            val preparedBody = prepareBody(order.body())
            val preparedEquipment = preparedEquipment(order.equipment())
            val bodyDeferred = async { combineBody(preparedBody, bodyLineOne, bodyLineTwo) }
            val equipmentDeferred = async { combineEquipment(preparedEquipment, equipmentLineOne, equipmentLineTwo) }
            val finalCompose = finalCompose(order, bodyDeferred.await(), equipmentDeferred.await())
            send(finalCompose)
        }
    }

private suspend fun prepareBody(body: Part.Body): ChosenBody {
    log("Preparing car body")
    delay(400)
    return ChosenBody(body)
}

private suspend fun preparedEquipment(equipment: Part.Equipment): ChosenEquipment {
    log("Preparing car body")
    delay(400)
    return ChosenEquipment(equipment)
}

private suspend fun finalCompose(
    order: Car,
    bodyPartsShot: BodyParts,
    equipment: EquipmentParts
): OutPut.FinishedCar {
    log("Combining parts")
    delay(100)
    return OutPut.FinishedCar(order, bodyPartsShot, equipment)
}