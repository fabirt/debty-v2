package com.fabirt.debty.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.fabirt.debty.R

sealed class MovementType(val multiplier: Int, @StringRes val name: Int, @ColorRes val color: Int) {
    object OwedMeSettled : MovementType(0, R.string.owed_me, R.color.colorNeutral)
    object IOwedSettled : MovementType(0, R.string.i_owed, R.color.colorNeutral)
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