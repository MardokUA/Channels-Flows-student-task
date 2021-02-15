package com.epam.functions.task1.utils

fun log(v: Any) = println("[${Thread.currentThread().name}] $v")

fun Float.format(digits: Int): String = String.format("%.${digits}f", this)

const val orderDesk = "orderDesk"
const val name = "Car factory"
const val BodyLine1 = "Body line 1"
const val BodyLine2 = "Body line 2"
const val EquipmentLine1 = "Equipment line 1"
const val EquipmentLine2 = "Equipment line 2"
const val ProvidedByConstructorTeam1 = "Provided by constructor team 1"
const val ProvidedByConstructorTeam2 = "Provided by constructor team 2"
const val combineBodyBodyLine1 = "combineBody bodyLine 1"
const val combineBodyBodyLine2 = "combineBody bodyLine 2"
const val combineEquipmentEquipmentLine1 = "combineEquipment equipmentLine 1"
const val combineEquipmentEquipmentLine2 = "combineEquipment equipmentLine 2"
