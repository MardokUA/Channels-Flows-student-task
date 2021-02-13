package com.epam.functions.task1

import com.epam.functions.task1.data.Car
import com.epam.functions.task1.data.Part
import com.epam.functions.task1.data.createBodyLine
import com.epam.functions.task1.data.equipmentLine
import com.epam.functions.task1.factory.createCar
import com.epam.functions.task1.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.onReceiveOrNull as onReceiveOrNullExt


/**
 * Let’s assume that you have a small automated workshop that produces cars on the automation line.
 * But it is not full automated and it should be observed by [CarConstructor].
 * Each machine uses **body line** and **equipment line** to construct a vehicle with [CarFactory].
 * And there is **order desk**, which collects the orders and starts the whole process.

Our workshop must have:
 * car factory ([CarFactory])
 * 2 constructor teams
 * 2 body lines ([BodyLines])
 * 2 equipment lines  ([EquipmentLines])

Our program should
 * Take an order from singe order list
 * Pick Constructor (in parallel)
 * Create body  (in parallel)
 * Create Equipment (in parallel)
 * Combine the body and equipment
 * Provide a car

 * Tips:
 * Please use channels to synchronise this processes.
 * Add time measurement
 */

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking(CoroutineName("com.epam.functions.task1.main")) {
    val orders = listOf(
        Car(Part.Body.Sedan, Part.Equipment.Premium),
        Car(Part.Body.SportCar, Part.Equipment.Family),
        Car(Part.Body.Sedan, Part.Equipment.LowCost),
        Car(Part.Body.Van, Part.Equipment.Premium),
        Car(Part.Body.Sedan, Part.Equipment.LowCost),
        Car(Part.Body.Van, Part.Equipment.LowCost),
        Car(Part.Body.Van, Part.Equipment.LowCost)
    )

    orders.forEach {
        log(it)
    }


    // TODO: remove method impl and add documentation
    val t = measureTimeMillis {
        val bodyLineOne = this.createBodyLine("Body line 1")
        val bodyLineTwo = this.createBodyLine("Body line 2")
        val equipmentLineOne = this.equipmentLine("Equipment line 1")
        val equipmentLineTwo = this.equipmentLine("Equipment line 2")
        // orders go into either car channel a or b (to be processed by one of the two constructors)
        // the result of these will get merged to be output here
        val ordersChannel = processOrders(orders)
        val carChannelA = createCar(
            orders = ordersChannel,
            scope = this,
            bodyLineOne = bodyLineOne,
            bodyLineTwo = bodyLineTwo,
            equipmentLineOne = equipmentLineOne,
            equipmentLineTwo = equipmentLineTwo
        )
        val carChannelB = createCar(
            orders = ordersChannel,
            scope = this,
            bodyLineOne = bodyLineOne,
            bodyLineTwo = bodyLineTwo,
            equipmentLineOne = equipmentLineOne,
            equipmentLineTwo = equipmentLineTwo
        )

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
                        if (v != null) log("Provided by constructor team 1 : $v")
                    }
                }
                if (isConstructorTwoActive) {
                    carChannelB.onReceiveOrNullExt().invoke { v ->
                        if (carChannelB.isClosedForReceive) isConstructorTwoActive = false
                        if (v != null) log("Provided by constructor team 2 : $v")
                    }
                }
            }
        }
        val isShotDown = shutdown(
            bodyLineOne = bodyLineOne,
            bodyLineTwo = bodyLineTwo,
            equipmentLineOne = equipmentLineOne,
            equipmentLineTwo = equipmentLineTwo
        )
        log("all channels are shotDown $isShotDown")
    }
    println("Execution time: $t ms")
}

//  producer of orders
// TODO: remove method impl and add documentation
@ExperimentalCoroutinesApi
private fun CoroutineScope.processOrders(orders: List<Car>) =
    produce(CoroutineName("orderDesk")) {
        for (order in orders) send(order)
    }
