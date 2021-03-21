package com.fabirt.debty.util

import java.text.NumberFormat

fun Number?.toCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance()
    return format.format(this ?: 0)
}