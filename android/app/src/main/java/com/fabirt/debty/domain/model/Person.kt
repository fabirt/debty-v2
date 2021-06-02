package com.fabirt.debty.domain.model

import android.graphics.Bitmap

data class Person(
    val name: String,
    val id: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val picture: Bitmap? = null,
    val total: Double? = null,
) {

    val indicator: SummaryIndicator
        get() = SummaryIndicator.from(total)
}