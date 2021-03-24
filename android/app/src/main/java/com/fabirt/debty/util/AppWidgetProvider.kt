package com.fabirt.debty.util

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import com.fabirt.debty.application.ResumeWidgetProvider

fun Activity.sendUpdateResumeWidgetBroadcast() {
    val appWidgetIds = AppWidgetManager.getInstance(this)
        .getAppWidgetIds(ComponentName(this, ResumeWidgetProvider::class.java))

    val intent = Intent(this, ResumeWidgetProvider::class.java).apply {
        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    }

    sendBroadcast(intent)
}