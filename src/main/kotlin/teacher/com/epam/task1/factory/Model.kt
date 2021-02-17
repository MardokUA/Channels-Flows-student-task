package teacher.com.epam.task1.factory

import teacher.com.epam.task1.data.Part

data class ChosenEquipment(val equipment: Part.Equipment) {
    override fun toString() = "chosenEquipment $equipment"
}

data class ChosenBody(val body: Part.Body) {
    override fun toString() = "chosenBody $body"
}