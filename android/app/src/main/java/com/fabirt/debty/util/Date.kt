package com.fabirt.debty.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(
    style: Int = SimpleDateFormat.MEDIUM
): String {
    val date = Date(this)
    return SimpleDateFormat.getDateInstance(style).format(date)
}