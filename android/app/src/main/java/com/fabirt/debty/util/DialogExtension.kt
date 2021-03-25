package com.fabirt.debty.util

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fabirt.debty.R

fun Fragment.showGeneralDialog(
    @StringRes title: Int,
    message: String,
    @StringRes positiveText: Int,
    @StringRes negativeText: Int = R.string.cancel,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ ->
            onConfirm?.invoke()
        }
        .setNegativeButton(negativeText) { _, _ ->
            onCancel?.invoke()
        }
        .create()
        .show()
}