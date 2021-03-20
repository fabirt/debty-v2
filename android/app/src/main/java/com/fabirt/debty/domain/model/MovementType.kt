package com.fabirt.debty.domain.model

sealed class MovementType {
    object Get : MovementType()
    object Give : MovementType()
    object Balance: MovementType()
}
