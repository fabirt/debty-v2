package com.fabirt.debty.util

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.showGeneralDialog(
    @StringRes titleId: Int,
    message: String,
    @StringRes positiveTextId: Int,
    @StringRes negativeTextId: Int,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    AlertDialog.Builder(requireContext())
        .setTitle(titleId)
        .setMessage(message)
        .setPositiveButton(positiveTextId) { _, _ ->
            onConfirm?.invoke()
        }
        .setNegativeButton(negativeTextId) { _, _ ->
            onCancel?.invoke()
        }
        .create()
        .show()
}