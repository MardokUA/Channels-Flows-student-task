package com.epam.functions

import com.epam.functions.data.Car
import com.epam.functions.data.Part
import com.epam.functions.factory.CarFactory
import com.epam.functions.utils.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.onReceiveOrNull as onReceiveOrNullExt


/**
 * Let’s assume that you have a small automated workshop that produces cars on the automation line.
 * But it is not full automated and it should be observed by [CarConstructor].
 * Each machine uses **body line** and **equipment line** to construct a vehicle with [CarFactory].
 * And there is **order desk**, which collects the orders and starts the whole process.

Our workshop must have:
 * 2 car factories
 * 2 body lines
 * 2 equipment lines

Our program should
 * Take an order
 * Pick Constructor
 * Create body  (in parallel)
 * Create Equipment (in parallel)
 * Combine the body and equipment
 * Provide a car

 * Tips:
 * Please use channels to synchronise this processes
 */

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking(CoroutineName("com.epam.functions.main")) {
    val orders = listOf(
        Car(Part.Body.Sedan, Part.Equipment.Premium),
        Car(Part.Body.SportCar, Part.Equipment.Family),
        Car(Part.Body.Sedan, Part.Equipment.LowCost),
        Car(Part.Body.Van, Part.Equipment.Premium),
        Car(Part.Body.Sedan, Part.Equipment.LowCost),
        Car(Part.Body.Van, Part.Equipment.LowCost)
    )
    orders.forEach { log(it) }

    val constructMachine = CarConstructor(this)

    val t = measureTimeMillis {
        // orders go into either car channel a or b (to be processed by one of the two constructors)
        // the result of these will get merged to be output here
        val ordersChannel = processOrders(orders)
        val carChannelA = CarFactory("constructor-1", this).createCar(ordersChannel, constructMachine)
        val carChannelB = CarFactory("constructor-2", this).createCar(ordersChannel, constructMachine)

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
                        if (carChannelA.isClosedForReceive) {
                            isConstructorOneActive = false
                        }
                        if (v != null) {
                            log("Provided: $v")
                        }
                    }
                }
                if (isConstructorTwoActive) {
                    carChannelA.onReceiveOrNullExt().invoke { v ->
                        if (carChannelB.isClosedForReceive) {
                            isConstructorTwoActive = false
                        }
                        if (v != null) {
                            log("Provided: $v")
                        }
                    }
                }
            }
        }
        constructMachine.shutdown()
    }
    println("Execution time: $t ms")
}

// convert this to a producer of orders
@ExperimentalCoroutinesApi
private fun CoroutineScope.processOrders(orders: List<Car>) =
    produce(CoroutineName("orderDesk")) {
        for (o in orders) send(o)
    }
