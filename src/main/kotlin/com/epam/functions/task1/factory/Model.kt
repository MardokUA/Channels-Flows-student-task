package com.epam.functions.task1.factory

import com.epam.functions.task1.data.Part

data class CompiledEquipment(val equipment: Part.Equipment){
    override fun toString() = "compiled_equipment"
}

data class ChosenBody(val body: Part.Body) {
    override fun toString() = "ground $body"
}