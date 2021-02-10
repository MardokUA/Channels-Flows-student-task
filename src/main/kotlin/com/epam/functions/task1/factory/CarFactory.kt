package com.epam.functions.task1.factory

import com.epam.functions.task1.CarConstructor
import com.epam.functions.task1.data.*
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

@ExperimentalCoroutinesApi
class CarFactory(private val name: String, private val scope: CoroutineScope) : CoroutineScope by scope {
    val bodyLineOne = bodyLineOne()
    val bodyLineTwo = bodyLineTwo()
    val equipmentLineOne = equipmentLineOne()
    val equipmentLineTwo = equipmentLineTwo()

    val carConstructor: CarConstructor = CarConstructor(
        scope = scope,
        bodyLineOne = bodyLineOne,
        bodyLineTwo = bodyLineTwo,
        equipmentLineOne = equipmentLineOne,
        equipmentLineTwo = equipmentLineTwo
    )

    // producer of completed car orders
    // TODO: remove method impl and add documentation
    fun createCar(orders: ReceiveChannel<Car>): ReceiveChannel<OutPut.FinishedCar> =
        scope.produce(CoroutineName(name)) {
            for (order in orders) {
                log("Processing order: $order")
                coroutineScope {
                    val preparedBody = prepareBody(order.body())
                    val bodyDeferred = async { carConstructor.combineBody(preparedBody) }
                    val equipmentDeferred = async { carConstructor.combineEquipment(order.equipment()) }
                    val finalCompose = finalCompose(order, bodyDeferred.await(), equipmentDeferred.await())
                    send(finalCompose)
                }
            }
        }

    fun shutdown(): Boolean = carConstructor.shutdown()

    private suspend fun prepareBody(body: Part.Body): ChosenBody {
        log("Preparing car body")
        delay(400)
        return ChosenBody(body)
    }

    private suspend fun finalCompose(
        order: Car,
        sparePartsShot: SpareParts,
        equipment: CompiledEquipment
    ): OutPut.FinishedCar {
        log("Combining parts")
        delay(100)
        return OutPut.FinishedCar(order, sparePartsShot, equipment)
    }
}