package com.epam.functions.data

import com.epam.functions.utils.format

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

        data class ChosenBody(val body: Body) : Body() {
            override fun price() = 0.00f
            override fun toString() = "ground $body"
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

    data class CompiledEquipment(val equipment: Equipment) : Part() {
        override fun price() = 0.00f
        override fun toString() = "compiled_equipment"
    }
}

data class SpareParts(val body: Part.Body.ChosenBody)

data class Car(val body: Part.Body, val equipment: Part.Equipment) {

    fun body() = body

    fun equipment() = equipment

    override fun toString() =
        "car: body=$body equipment=$equipment price=$${(body.price() + equipment.price()).format(2)}"
}


sealed class OutPut {
    data class FinishedCar(
        val order: Car,
        val sparePartsShot: SpareParts,
        val compiledEquipment: Part.CompiledEquipment
    ) : OutPut()
}
