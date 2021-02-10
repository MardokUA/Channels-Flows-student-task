package com.epam.functions.task1.data

import com.epam.functions.task1.factory.ChosenBody
import com.epam.functions.task1.factory.CompiledEquipment
import com.epam.functions.task1.utils.format

sealed class Part {

    abstract fun price(): Float

    sealed class Body : Part() {

        object SportCar : Body() {
            override fun price() = 100000.00f
            override fun toString() = "sport_car"
        }

        object Sedan : Body() {
            override fun price() = 20000.00f
            override fun toString() = "sedan"
        }

        object Van : Body() {
            override fun price() = 30000.50f
            override fun toString() = "van"
        }
    }

    sealed class Equipment : Part() {

        object Premium : Equipment() {
            override fun price() = 100000.00f
            override fun toString() = "premium"
        }

        object LowCost : Equipment() {
            override fun price() = 20000.00f
            override fun toString() = "low_cost"
        }

        object Family : Equipment() {
            override fun price() = 30000.00f
            override fun toString() = "family"
        }
    }
}

data class SpareParts(val body: ChosenBody)

data class Car(val body: Part.Body, val equipment: Part.Equipment) {

    fun body() = body
    fun equipment() = equipment

    override fun toString() = "car: body=$body " +
            "equipment=$equipment " +
            "price=$${(body.price() + equipment.price()).format(2)}"
}


sealed class OutPut {
    data class FinishedCar(
        val order: Car,
        val sparePartsShot: SpareParts,
        val compiledEquipment: CompiledEquipment
    ) : OutPut()
}
