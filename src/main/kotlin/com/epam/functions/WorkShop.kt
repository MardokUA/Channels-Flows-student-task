package com.epam.functions

import com.epam.functions.data.*
import com.epam.functions.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.onReceiveOrNull as onReceiveOrNullExt


/**
 * Let’s assume that you have a small automated workshop that produces a cars on the automation line.
But it is not full automated - it should be observed by “Constructor” - now you have 2
Also you have 2 body lines and 2 equipment lines
And one order desk - that collects the orders and starts the whole process.
Our program should
 * Take an order
 * Pick Constructor
 * Create body  (in parallel)
 * Create Equipment (in parallel)
 * Combine the body and  equipment
 * Provide a car

Please use channels to synchronise this processes */

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking(CoroutineName("com.epam.functions.main")) {
    val orders = listOf(
        Products.Car(Body.Sedan, Equipment.Premium),
        Products.Car(Body.SportCar, Equipment.Family),
        Products.Car(Body.Sedan, Equipment.LowCost),
        Products.Car(Body.Van, Equipment.Premium),
        Products.Car(Body.Sedan, Equipment.LowCost),
        Products.Car(Body.Van, Equipment.LowCost)
    )
    log(orders)

    val constructMachine = ConstructMachine(this)
    val t = measureTimeMillis {
        // orders go into either car channel a or b (to be processed by one of the two constructors)
        // the result of these will get merged to be output here
        val ordersChannel = processOrders(orders)
        val carChannelA = createCar("constructor-1", ordersChannel, constructMachine)
        val carChannelB = createCar("constructor-2", ordersChannel, constructMachine)

        // as of right now there's no 'onReceiveOrClosed' operator so we need to track this manually
        // if the carChannel[A|B] was closed, then onReceiveOrNull is fired on each loop rather
        // than suspending
        // this switches on receive from the two constructors, when an order arrives, we print it here
        var isConstructorOneActive = true
        var isConstructorTwoActive = true
        while (isConstructorOneActive || isConstructorTwoActive) {
            select<Unit> {
                if (isConstructorOneActive) {
                    carChannelA.onReceiveOrNullExt().invoke { v ->
                        if (carChannelA.isClosedForReceive) isConstructorOneActive = false
                        if (v != null) log("Provided 1 : $v")
                    }
                }
                if (isConstructorTwoActive) {
                    carChannelB.onReceiveOrNullExt().invoke { v ->
                        if (carChannelB.isClosedForReceive) isConstructorTwoActive = false
                        if (v != null) log("Provided 2 : $v")
                    }
                }
            }
        }
        constructMachine.shutdown()
    }
    println("Execution time: $t ms")
}

// convert this to a producer of orders
private fun CoroutineScope.processOrders(orders: List<Products>) = produce(CoroutineName("orderDesk")) {
    for (o in orders) send(o)
}

// convert this to a producer of completed car orders
private fun CoroutineScope.createCar(
    tag: String,
    orders: ReceiveChannel<Products>,
    constructMachine: ConstructMachine
) = produce(CoroutineName(tag)) {
    for (o in orders) {
        log("Processing order: $o")
        when (o) {
            is Products.Car -> {
                val preparedBody = prepareBody(o.body())
                coroutineScope {
                    val bodyDeferred = async { constructMachine.combineBody(preparedBody) }
                    val equipmentDeferred = async { constructMachine.combineEquipment(o.equipment()) }
                    val finalCompose = finalCompose(o, bodyDeferred.await(), equipmentDeferred.await())
                    send(finalCompose)
                }
            }
        }
    }
}

private suspend fun prepareBody(body: Body): Body.ChoosedBody {
    log("Preparing car body")
    delay(30)
    return Body.ChoosedBody(body)
}

private suspend fun finalCompose(
    order: Products.Car,
    sparePartsShot: SpareParts,
    equipment: Equipment.CompiledEquipment
): OutPut.FinishedCar {
    log("Combining parts")
    delay(5)
    return OutPut.FinishedCar(order, sparePartsShot, equipment)
}
