package com.fabirt.debty.util

import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.applyStatusBarTopInset() {
    setOnApplyWindowInsetsListener { v, insets ->
        val statusBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.statusBars())

        v.updatePadding(top = statusBarInsets.top)

        insets
    }
}

fun View.applyNavigationBarBottomInset() {
    setOnApplyWindowInsetsListener { v, insets ->
        val navigationBarInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.navigationBars())

        v.updatePadding(bottom = navigationBarInsets.bottom)

        insets
    }
}