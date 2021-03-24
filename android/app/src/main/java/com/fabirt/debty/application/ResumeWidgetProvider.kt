package com.fabirt.debty.application

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.fabirt.debty.R
import com.fabirt.debty.data.db.dao.MovementDao
import com.fabirt.debty.ui.MainActivity
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@AndroidEntryPoint
class ResumeWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var dao: MovementDao

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }

            GlobalScope.launch {
                val totalOweMe = dao.getTotalOweMe()?.absoluteValue ?: 0.0
                val totalIOwe = dao.getTotalIOwe()?.absoluteValue ?: 0.0

                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                val views: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.widget_resume
                ).apply {
                    setOnClickPendingIntent(R.id.container, pendingIntent)
                    setTextViewText(R.id.tv_owe_me_value, totalOweMe.toCurrencyString())
                    setTextViewText(R.id.tv_i_owe_value, totalIOwe.toCurrencyString())
                }

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}