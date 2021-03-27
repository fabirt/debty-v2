package com.fabirt.debty.domain.model

data class Movement(
    val id: Int,
    val personId: Int,
    val amount: Double,
    /**
     * Time in milliseconds since Epoch.
     */
    val date: Long,
    val description: String,
    val type: MovementType
)