package com.fabirt.debty.util

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.fragment.app.Fragment
import com.fabirt.debty.application.SummaryWidgetProvider

fun Activity.sendUpdateAppWidgetBroadcast() {
    val appWidgetIds = AppWidgetManager.getInstance(this)
        .getAppWidgetIds(ComponentName(this, SummaryWidgetProvider::class.java))

    val intent = Intent(this, SummaryWidgetProvider::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    }

    sendBroadcast(intent)
}

fun Fragment.sendUpdateAppWidgetBroadcast() {
    requireActivity().sendUpdateAppWidgetBroadcast()
}