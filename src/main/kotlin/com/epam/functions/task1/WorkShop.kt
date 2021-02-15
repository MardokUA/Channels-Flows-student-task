package com.epam.functions.task1

import com.epam.functions.task1.data.Car
import com.epam.functions.task1.data.createBodyLine
import com.epam.functions.task1.data.createEquipmentLine
import com.epam.functions.task1.factory.createCar
import com.epam.functions.task1.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.onReceiveOrNull as onReceiveOrNullExt


/**
 * Let’s assume that you have a small automated workshop that produces cars on the automation line.
 * But it is not full automated and it should be observed by [CarConstructor.kt].
 * Each machine uses **body line** and **equipment line** to construct a vehicle with [CarFactory.kt].
 * And there is **order desk**, which collects the orders and starts the whole process.

Our workshop must have:
 * car factory ([CarFactory.kt])
 * 2 constructor teams
 * 2 body lines ([BodyLines.kt])
 * 2 equipment lines  ([EquipmentLines.kt])

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
fun startWorkShopWork(orders: List<Car>) = runBlocking(CoroutineName("com.epam.functions.task1.main")) {


    // TODO: remove method impl and add documentation
    val t = measureTimeMillis {
        val bodyLineOne = this.createBodyLine(BodyLine1)
        val bodyLineTwo = this.createBodyLine(BodyLine2)
        val equipmentLineOne = this.createEquipmentLine(EquipmentLine1)
        val equipmentLineTwo = this.createEquipmentLine(EquipmentLine2)

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
                        if (v != null) log("$ProvidedByConstructorTeam1 : $v")
                    }
                }
                if (isConstructorTwoActive) {
                    carChannelB.onReceiveOrNullExt().invoke { v ->
                        if (carChannelB.isClosedForReceive) isConstructorTwoActive = false
                        if (v != null) log("$ProvidedByConstructorTeam2 : $v")
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

//  creates ReceiveChannel to emit orders for constructors teams.
// TODO: remove method impl and add documentation
@ExperimentalCoroutinesApi
private fun CoroutineScope.processOrders(orders: List<Car>) =
    produce(CoroutineName(orderDesk)) {
        for (order in orders) send(order)
    }
