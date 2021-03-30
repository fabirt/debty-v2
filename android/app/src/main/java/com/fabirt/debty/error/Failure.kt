package com.fabirt.debty.error

import android.content.Context
import androidx.annotation.StringRes
import com.fabirt.debty.R

sealed class Failure(@StringRes val key: Int) {

    object UnexpectedFailure : Failure(R.string.unexpected_error_message)

    fun translate(context: Context): String {
        return context.getString(key)
    }
}