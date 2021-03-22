package com.fabirt.debty.domain.model

import android.graphics.Bitmap

data class Person(
    val id: Int,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val picture: Bitmap? = null,
    val total: Double? = null,
) {

    val indicator: SummaryIndicator
        get() = SummaryIndicator.from(total)
}