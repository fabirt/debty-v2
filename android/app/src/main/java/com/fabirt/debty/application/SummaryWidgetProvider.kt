package com.fabirt.debty.application

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.fabirt.debty.R
import com.fabirt.debty.domain.repository.person.PersonRepository
import com.fabirt.debty.ui.MainActivity
import com.fabirt.debty.util.calculateSummaryData
import com.fabirt.debty.util.toCurrencyString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SummaryWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var repository: PersonRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.i("ResumeWidgetProvider", "onUpdate")
        GlobalScope.launch {
            val summaryData = withContext(Dispatchers.IO) {
                val people = repository.oneTimeRequestAllPersonsWithTotal()
                calculateSummaryData(people)
            }

            // Perform this loop procedure for each App Widget that belongs to this provider
            appWidgetIds.forEach { appWidgetId ->
                // Create an Intent to launch MainActivity
                val intentActivity = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val pendingIntentActivity =
                    PendingIntent.getActivity(
                        context,
                        0,
                        intentActivity,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                // val pendingIntentUpdate = getUpdateSelfPendingIntent(context, appWidgetIds)

                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                val views: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.appwidget_summary
                ).apply {
                    setTextViewText(
                        R.id.tv_owe_me_value,
                        summaryData.positive.toCurrencyString()
                    )
                    setTextViewText(
                        R.id.tv_i_owe_value,
                        summaryData.negative.toCurrencyString()
                    )

                    setOnClickPendingIntent(R.id.container, pendingIntentActivity)
                    // setOnClickPendingIntent(R.id.btn_refresh_widget, pendingIntentUpdate)
                }

                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    private fun getUpdateSelfPendingIntent(
        context: Context,
        appWidgetIds: IntArray
    ): PendingIntent {
        val intent = Intent(context, SummaryWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}