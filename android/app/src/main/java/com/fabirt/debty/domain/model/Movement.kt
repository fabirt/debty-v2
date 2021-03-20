package com.fabirt.debty.domain.model

data class Movement(
    val id: Int,
    val personId: Int,
    val amount: Double,
    val epochMilli: Long,
    val description: String,
    val type: MovementType
)