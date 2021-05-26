package com.fabirt.debty.domain.model

import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import com.fabirt.debty.R

data class SummaryIndicator(
    @StringRes val stringId: Int,
    @AttrRes val colorAttrId: Int
) {
    companion object {
        fun from(value: Double?): SummaryIndicator {
            return value?.let {
                if (it > 0) {
                    SummaryIndicator(R.string.owe_me, R.attr.colorTransactionPositive)
                } else {
                    SummaryIndicator(R.string.i_owe, R.attr.colorTransactionNegative)
                }
            } ?: SummaryIndicator(R.string.owe_me, R.attr.colorTransactionPositive)
        }
    }
}