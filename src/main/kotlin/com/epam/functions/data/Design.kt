package com.epam.functions.data

import com.epam.functions.utils.format

sealed class Body {
    abstract fun price(): Float

    object SportCar: Body() {
        override fun price() = 100000.00f
        override fun toString() = "sport_car"
    }

    object Sedan: Body() {
        override fun price() = 20000.00f
        override fun toString() = "sedan"
    }

    object Van: Body() {
        override fun price() = 30000.50f
        override fun toString() = "van"
    }

    data class ChoosedBody(val body: Body): Body() {
        override fun price() = 0.00f
        override fun toString() = "ground $body"
    }
}

sealed class Equipment {
    abstract fun price(): Float

    object Premium: Equipment() {
        override fun price() = 100000.00f

        override fun toString() = "premium"
    }

    object LowCost: Equipment() {
        override fun price() = 20000.00f

        override fun toString() = "low_cost"
    }

    object Family: Equipment() {
        override fun price() = 30000.00f

        override fun toString() = "family"
    }
    data class CompiledEquipment(val equipment: Equipment): Equipment() {
        override fun price() = 0.00f

        override fun toString() = "compiled_equipment"
    }
}

data class SpareParts(val body: Body.ChoosedBody)

sealed class Products {
    abstract fun price(): Float

    abstract fun body(): Body

    abstract fun equipment(): Equipment

    data class Car(val body: Body, val equipment: Equipment): Products() {
        override fun price() = body.price() + equipment.price()

        override fun body() = body

        override fun equipment() = equipment

        override fun toString() = "car: body=$body equipment=$equipment price=$${price().format(2)}"
    }
}

sealed class OutPut {
    data class FinishedCar(val order: Products.Car, val sparePartsShot: SpareParts, val compiledEquipment: Equipment.CompiledEquipment): OutPut()
}
