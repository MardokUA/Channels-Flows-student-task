package com.epam.functions.task1.utils

fun log(v: Any) = println("[${Thread.currentThread().name}] $v")

fun Float.format(digits: Int): String = String.format("%.${digits}f", this)