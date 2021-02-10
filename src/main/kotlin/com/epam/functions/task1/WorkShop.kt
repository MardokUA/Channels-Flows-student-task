package com.epam.functions.task1

import com.epam.functions.task1.data.Car
import com.epam.functions.task1.data.Part
import com.epam.functions.task1.factory.CarFactory
import com.epam.functions.task1.utils.log
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
 * car factory ([CarFactory])
 * 2 body ([BodyLines])
 * 2 equipment lines  ([EquipmentLines])

Our program should
 * Take an order
 * Pick Constructor
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
        Car(Part.Body.Van, Part.Equipment.LowCost)
    )

    orders.forEach {
        log(it)
    }

    // TODO: remove method impl and add documentation
    val t = measureTimeMillis {
        val ordersChannel = processOrders(orders)
        val factory = CarFactory("constructor-1", this)
        factory.createCar(ordersChannel).consumeEach {
            log("Provided: $it")
        }
        val isShutdown = factory.shutdown()
        log("Factory is shutdown: $isShutdown")
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
