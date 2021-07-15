package com.fabirt.debty.util

import android.app.Activity
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.fabirt.debty.R
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView

fun Activity.showSingleTapTargetView(
    view: View,
    title: String,
    description: String,
    cancelable: Boolean = false,
    onTargetClick: (() -> Unit)? = null,
    onTargetCancel: (() -> Unit)? = null
): TapTargetView {
    val typeface = ResourcesCompat.getFont(applicationContext, R.font.montserrat_medium)

    val target = TapTarget
        .forView(view, title, description)
        .textTypeface(typeface)
        .tintTarget(false)
        .cancelable(cancelable)

    val listener = object : TapTargetView.Listener() {
        override fun onTargetClick(view: TapTargetView?) {
            super.onTargetClick(view)
            onTargetClick?.invoke()
        }

        override fun onTargetCancel(view: TapTargetView?) {
            super.onTargetCancel(view)
            onTargetCancel?.invoke()
        }
    }

    return TapTargetView.showFor(this, target, listener)
}

fun Activity.showMultiTapTargetView(
    vararg targets: TapTarget
) {
    TapTargetSequence(this)
        .targets(*targets)
        .start()
}