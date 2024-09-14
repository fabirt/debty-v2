package com.fabirt.debty.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun Number?.toCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}

fun Number?.toCurrencyString2(): String {
    val symbols = DecimalFormatSymbols.getInstance(Locale.US)
    val format = DecimalFormat("\$#,##0", symbols)
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}

fun Number?.toDecimalString(): String {
    val symbols = DecimalFormatSymbols.getInstance(Locale.US)
    val format = DecimalFormat("#,##0", symbols)
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}

fun String.removeGroupingSeparator(): String {
    val symbols = DecimalFormatSymbols.getInstance(Locale.US)
    return replace(symbols.groupingSeparator.toString(), "")
}
