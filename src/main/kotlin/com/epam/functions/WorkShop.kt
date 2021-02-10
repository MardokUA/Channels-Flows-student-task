package com.epam.functions

import com.epam.functions.data.Car
import com.epam.functions.data.Part
import com.epam.functions.factory.CarFactory
import com.epam.functions.utils.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


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
 * Please use channels to synchronise this processes.
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

    orders.forEach {
        log(it)
    }

    val t = measureTimeMillis {
        val ordersChannel = processOrders(orders)
        val factory = CarFactory("constructor-1", this)
        factory.createCar(ordersChannel).consumeEach {
            log("Provided: $it")
        }
    }

    println("Execution time: $t ms")
}

// convert this to a producer of orders
@ExperimentalCoroutinesApi
private fun CoroutineScope.processOrders(orders: List<Car>) =
    produce(CoroutineName("orderDesk")) {
        for (order in orders) send(order)
    }
