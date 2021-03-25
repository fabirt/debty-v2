package com.fabirt.debty.util

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.fabirt.debty.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showGeneralDialog(
    @StringRes title: Int,
    message: String,
    @StringRes positiveText: Int,
    @StringRes negativeText: Int = R.string.cancel,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ ->
            onConfirm?.invoke()
        }
        .setNegativeButton(negativeText) { _, _ ->
            onCancel?.invoke()
        }
        .show()
}