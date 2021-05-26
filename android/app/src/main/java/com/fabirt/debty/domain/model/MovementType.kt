package com.fabirt.debty.domain.model

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.fabirt.debty.R

sealed class MovementType(
    val multiplier: Int,
    @StringRes val name: Int,
    @AttrRes val colorAttrId: Int,
) {
    object OwedMeSettled : MovementType(0, R.string.owed_me, R.attr.colorTransactionNeutral)
    object IOwedSettled : MovementType(0, R.string.i_owed, R.attr.colorTransactionNeutral)
    object ILent : MovementType(1, R.string.i_lent, R.attr.colorTransactionNegative)
    object IPaid : MovementType(1, R.string.i_paid, R.attr.colorTransactionPositive)
    object LentMe : MovementType(-1, R.string.lent_me, R.attr.colorTransactionNegative)
    object PaidMe : MovementType(-1, R.string.paid_me, R.attr.colorTransactionPositive)

    val isLoan: Boolean
        get() = this is ILent || this is LentMe

    val isSettled: Boolean
        get() = this is OwedMeSettled || this is IOwedSettled

}

val movementTypeOptions = listOf(
    MovementType.ILent,
    MovementType.IPaid,
    MovementType.LentMe,
    MovementType.PaidMe
)