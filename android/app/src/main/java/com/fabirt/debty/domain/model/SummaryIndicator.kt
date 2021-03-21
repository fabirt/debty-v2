package com.fabirt.debty.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.fabirt.debty.R

data class SummaryIndicator(
    @StringRes val stringId: Int,
    @ColorRes val colorId: Int
) {
    companion object {
        fun from(value: Double?): SummaryIndicator {
            return value?.let {
                if (it > 0) {
                    SummaryIndicator(R.string.owe_me, R.color.colorCustom1)
                } else {
                    SummaryIndicator(R.string.i_owe, R.color.colorCustom2)
                }
            } ?: SummaryIndicator(R.string.owe_me, R.color.colorCustom1)
        }
    }
}