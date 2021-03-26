package com.fabirt.debty.ui.common

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import kotlin.math.min

class FloatingActionButtonBehavior @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>() {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val translationY = min(0F, dependency.translationY - dependency.height)
        // child.translationY = translationY
        animateTranslationY(child, translationY)
        return true
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View, dependency: View) {
        super.onDependentViewRemoved(parent, child, dependency)
        // child.translationY = 0F
        animateTranslationY(child, 0F)
    }

    private fun animateTranslationY(view: View, translation: Float) {
        ObjectAnimator.ofFloat(view, "translationY", translation).apply {
            duration = 150 // milliseconds
            start()
        }
    }
}