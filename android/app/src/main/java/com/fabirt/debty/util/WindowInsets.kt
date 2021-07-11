package com.fabirt.debty.util

import android.graphics.Insets
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

fun View.applySystemBarsPadding() {
    setOnApplyWindowInsetsListener { v, insets ->
        val windowInsets = WindowInsetsCompat.toWindowInsetsCompat(insets)

        val statusBarInsets = windowInsets
            .getInsets(WindowInsetsCompat.Type.statusBars())
        val navigationBarInsets = windowInsets
            .getInsets(WindowInsetsCompat.Type.navigationBars())

        v.updatePadding(top = statusBarInsets.top, bottom = navigationBarInsets.bottom)

        insets
    }
}

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

@RequiresApi(Build.VERSION_CODES.R)
class RootViewDeferringInsetsCallback(
    private val persistentInsetTypes: Int,
    private val deferredInsetTypes: Int
) : WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE), View.OnApplyWindowInsetsListener {

    private var view: View? = null
    private var lastWindowInsets: WindowInsets? = null
    private var deferredInsets = false

    override fun onProgress(
        insets: WindowInsets,
        runningAnimations: List<WindowInsetsAnimation>
    ): WindowInsets {
        return insets
    }

    override fun onApplyWindowInsets(v: View?, windowInsets: WindowInsets?): WindowInsets {
        view = v
        lastWindowInsets = windowInsets
        val types = when {
            deferredInsets -> persistentInsetTypes
            else -> persistentInsetTypes or deferredInsetTypes
        }

        val typeInsets = windowInsets!!.getInsets(types)
        v?.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)

        return WindowInsets.CONSUMED
    }

    override fun onPrepare(animation: WindowInsetsAnimation) {
        if (animation.typeMask and deferredInsetTypes != 0) {
            deferredInsets = true
        }
    }

    override fun onEnd(animation: WindowInsetsAnimation) {
        if (deferredInsets && (animation.typeMask and deferredInsetTypes) != 0) {
            deferredInsets = false
            if (lastWindowInsets != null) {
                view?.dispatchApplyWindowInsets(lastWindowInsets!!)
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.R)
class TranslateDeferringInsetsAnimationCallback constructor(
    private val view: View,
    private val persistentInsetTypes: Int,
    private val deferredInsetTypes: Int,
    dispatchMode: Int = DISPATCH_MODE_STOP
) : WindowInsetsAnimation.Callback(dispatchMode) {
    init {
        require(persistentInsetTypes and deferredInsetTypes == 0) {
            "persistentInsetTypes and deferredInsetTypes can not contain any of " +
                    " same WindowInsets.Type values"
        }
    }

    override fun onProgress(
        insets: WindowInsets,
        runningAnimations: List<WindowInsetsAnimation>
    ): WindowInsets {
        val typesInset = insets.getInsets(deferredInsetTypes)
        val otherInset = insets.getInsets(persistentInsetTypes)
        val diff = Insets.subtract(typesInset, otherInset).let {
            Insets.max(it, Insets.NONE)
        }
        view.translationX = (diff.left - diff.right).toFloat()
        view.translationY = (diff.top - diff.bottom).toFloat()
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimation) {
        view.translationX = 0f
        view.translationY = 0f
    }
}