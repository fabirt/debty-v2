package com.fabirt.debty.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.fabirt.debty.R

sealed class MovementType(val multiplier: Int, @StringRes val name: Int, @ColorRes val color: Int) {
    object Incoming : MovementType(-1, R.string.movement_incoming, R.color.colorCustom2)
    object Outgoing : MovementType(1, R.string.movement_outgoing, R.color.colorCustom1)
    object Balance : MovementType(0, R.string.balance, R.color.colorPrimary)
}

val movementTypeOptions = listOf(MovementType.Incoming, MovementType.Outgoing)