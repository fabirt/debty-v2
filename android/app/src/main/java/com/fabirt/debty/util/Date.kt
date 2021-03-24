package com.fabirt.debty.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(
    style: Int = SimpleDateFormat.MEDIUM
): String {
    val date = Date(this)
    return SimpleDateFormat.getDateInstance(style).format(date)
}

fun Long.utcTimeToLocaleTime(): Long? {
    val utcTime = Date(this)
    val format = "yyy/MM/dd HH:mm:ss"
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val gmtTime = SimpleDateFormat(format, Locale.getDefault()).parse(sdf.format(utcTime))
    return gmtTime?.time
}

fun Long.toUtcTime(): Long? {
    val local = Date(this)
    val format = "yyy/MM/dd HH:mm:ss"
    val localDateString = SimpleDateFormat(format, Locale.getDefault()).format(local)
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val utcTime = sdf.parse(localDateString)
    return utcTime?.time
}