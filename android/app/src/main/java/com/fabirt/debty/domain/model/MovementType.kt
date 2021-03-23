package com.fabirt.debty.domain.model

import androidx.annotation.StringRes
import com.fabirt.debty.R

sealed class MovementType(@StringRes val name: Int, val multiplier: Int) {
    object Incoming : MovementType(R.string.movement_incoming, -1)
    object Outgoing : MovementType(R.string.movement_outgoing, 1)
    object Balance : MovementType(R.string.balance, 0)
}

val movementTypeOptions = listOf(MovementType.Incoming, MovementType.Outgoing)