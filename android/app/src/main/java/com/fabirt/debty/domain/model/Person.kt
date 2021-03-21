package com.fabirt.debty.domain.model

import android.graphics.Bitmap

data class Person(
    val id: Int,
    val name: String,
    val picture: Bitmap? = null,
    val total: Double? = null,
) {

    val indicator: SummaryIndicator
        get() = SummaryIndicator.from(total)
}