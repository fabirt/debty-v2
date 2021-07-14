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
    onTargetClick: () -> Unit
) {
    val typeface = ResourcesCompat.getFont(applicationContext, R.font.montserrat_medium)

    val target = TapTarget
        .forView(view, title, description)
        .textTypeface(typeface)
        .tintTarget(false)
        .cancelable(false)

    val listener = object : TapTargetView.Listener() {
        override fun onTargetClick(view: TapTargetView?) {
            super.onTargetClick(view)
            onTargetClick()
        }
    }

    TapTargetView.showFor(this, target, listener)
}

fun Activity.showMultiTapTargetView(
    vararg targets: TapTarget
) {
    TapTargetSequence(this)
        .targets(*targets)
        .start()
}