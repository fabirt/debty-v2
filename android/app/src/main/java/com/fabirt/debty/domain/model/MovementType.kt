package com.fabirt.debty.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.fabirt.debty.R

sealed class MovementType(val multiplier: Int, @StringRes val name: Int, @ColorRes val color: Int) {
    object Balance : MovementType(0, R.string.balance, R.color.colorPrimary)
    object ILent : MovementType(1, R.string.i_lent, R.color.colorNegative)
    object IPaid : MovementType(1, R.string.i_paid, R.color.colorPositive)
    object LentMe : MovementType(-1, R.string.lent_me, R.color.colorNegative)
    object PaidMe : MovementType(-1, R.string.paid_me, R.color.colorPositive)
}

val movementTypeOptions = listOf(
    MovementType.ILent,
    MovementType.IPaid,
    MovementType.LentMe,
    MovementType.PaidMe
)