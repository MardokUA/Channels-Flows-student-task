package com.epam.functions.factory

import com.epam.functions.data.Part

data class CompiledEquipment(val equipment: Part.Equipment){
    override fun toString() = "compiled_equipment"
}

data class ChosenBody(val body: Part.Body) {
    override fun toString() = "ground $body"
}