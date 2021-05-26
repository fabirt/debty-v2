package com.fabirt.debty.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.play.core.review.ReviewManagerFactory

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Activity.requestReview() {
    val manager = ReviewManagerFactory.create(this)
    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            manager.launchReviewFlow(this, reviewInfo)
        }
    }
}