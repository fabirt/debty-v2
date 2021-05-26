package com.fabirt.debty.util

import java.text.DecimalFormat
import java.text.NumberFormat

fun Number?.toCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance()
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}

fun Number?.toCurrencyString2(): String {
    val format = DecimalFormat("\$#,##0")
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}

fun Number?.toDecimalString(): String {
    val format = DecimalFormat("#,##0")
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}