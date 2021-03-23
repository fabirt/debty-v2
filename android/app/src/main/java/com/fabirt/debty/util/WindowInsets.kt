package com.fabirt.debty.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

fun View.applyStatusBarTopInset() {
    setOnApplyWindowInsetsListener { v, insets ->
        val statusBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.statusBars())

        v.updatePadding(top = statusBarInsets.top)

        insets
    }
}

fun View.applyNavigationBarBottomInset(extraPadding: Int = 0) {
    setOnApplyWindowInsetsListener { v, insets ->
        val navigationBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.navigationBars())

        v.updatePadding(bottom = navigationBarInsets.bottom + extraPadding.dp)

        insets
    }
}

fun View.applyNavigationBarBottomMargin(extraMargin: Int = 0) {
    setOnApplyWindowInsetsListener { v, insets ->
        val navigationBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.navigationBars())

        v.updateLayoutParams {
            (this as? ViewGroup.MarginLayoutParams)?.setMargins(
                0,
                0,
                0,
                navigationBarInsets.bottom + extraMargin.dp
            )

        }

        insets
    }
}