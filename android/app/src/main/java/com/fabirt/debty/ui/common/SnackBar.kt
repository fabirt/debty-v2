package com.fabirt.debty.ui.common

import android.view.View
import androidx.fragment.app.Fragment
import com.fabirt.debty.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
}

fun showSnackBar(text: String, contextView: View) {
    Snackbar
        .make(contextView, text, Snackbar.LENGTH_LONG)
        .show()
}

fun showSnackBar(text: String, contextView: View, anchorView: View) {
    Snackbar
        .make(contextView, text, Snackbar.LENGTH_LONG)
        .setAnchorView(anchorView)
        .show()
}

fun showSnackBarWithAction(
    text: String,
    contextView: View,
    actionText: String,
    onPressed: View.OnClickListener
) {
    Snackbar
        .make(contextView, text, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionText, onPressed)
        .show()
}


fun Fragment.showUnexpectedFailureSnackBar() {
    showSnackBar(getString(R.string.unexpected_error_message))
}