package com.fabirt.debty.ui.common

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
}