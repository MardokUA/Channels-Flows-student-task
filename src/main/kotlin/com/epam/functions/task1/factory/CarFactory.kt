package com.epam.functions.task1.factory

import com.epam.functions.task1.PrepareBodyRequest
import com.epam.functions.task1.PrepareEquipmentRequest
import com.epam.functions.task1.combineBody
import com.epam.functions.task1.combineEquipment
import com.epam.functions.task1.data.*
import com.epam.functions.task1.utils.log
import com.epam.functions.task1.utils.name
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce


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
            val bodyDeferred = async { combineBody(preparedBody, bodyLineOne, bodyLineTwo) }
            val equipmentDeferred = async { combineEquipment(preparedEquipment, equipmentLineOne, equipmentLineTwo) }
            val finalCompose = finalCompose(order, bodyDeferred.await(), equipmentDeferred.await())
            send(finalCompose)
        }
    }

private suspend fun prepareBody(body: Part.Body): ChosenBody {
    log("Preparing car body $body")
    delay(400)
    return ChosenBody(body)
}

private suspend fun preparedEquipment(equipment: Part.Equipment): ChosenEquipment {
    log("Preparing car equipment $equipment")
    delay(400)
    return ChosenEquipment(equipment)
}

// composes provided BodyParts and EquipmentParts to FinishedCar
private suspend fun finalCompose(
    order: Car,
    bodyParts: BodyParts,
    equipment: EquipmentParts
): OutPut.FinishedCar {
    log("Combining parts $bodyParts, $equipment")
    delay(100)
    return OutPut.FinishedCar(order, bodyParts, equipment)
}