package com.fabirt.debty.ui

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.fabirt.debty.R
import com.fabirt.debty.constant.K
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val APP_UPDATE_REQUEST_CODE = 12
    }

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_App)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        // Not recreating the activity.
        if (savedInstanceState == null) {
            handleIntent()
        }
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
    }


    private fun handleIntent() {
        intent?.dataString?.let { data ->
            when (data) {
                K.SHORTCUT_DATA_MOVEMENT_ASSISTANT -> triggerNewMovementDeepLink()
            }
        }
    }

    private fun triggerNewMovementDeepLink() {
        lifecycleScope.launch {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.personSearchFragment)
        }
    }

    private fun checkUpdate() {
        Log.i(TAG, "checkUpdate")
        val updateInfoTask = appUpdateManager.appUpdateInfo

        updateInfoTask.addOnSuccessListener { updateInfo ->
            val availability = updateInfo.updateAvailability()
            if (availability == UpdateAvailability.UPDATE_AVAILABLE) {
                startUpdateFlow(appUpdateManager, updateInfo)
            } else if (availability == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateManager, updateInfo)
            }
            if (updateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate()
            }
        }.addOnFailureListener { error ->
            Log.e(TAG, error.toString())
        }
    }

    private fun startUpdateFlow(updateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
        updateManager.registerListener(object : InstallStateUpdatedListener {
            override fun onStateUpdate(state: InstallState) {
                when (state.installStatus()) {
                    InstallStatus.DOWNLOADING -> {
                        showDownloadStartNotification()
                    }
                    InstallStatus.DOWNLOADED -> {
                        showDownloadedNotification()
                        updateManager.unregisterListener(this)
                    }
                    InstallStatus.FAILED -> {
                        showDownloadErrorNotification()
                        updateManager.unregisterListener(this)
                    }
                    InstallStatus.CANCELED -> {
                        showDownloadErrorNotification()
                        updateManager.unregisterListener(this)
                    }
                    else -> {
                    }
                }
            }
        })

        updateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.FLEXIBLE,
            this,
            APP_UPDATE_REQUEST_CODE
        )
    }

    private fun showDownloadStartNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, K.APP_UPDATE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_download)
            .setColor(getColor(R.color.colorPrimary))
            .setContentTitle(getString(R.string.app_update_downloading))
            .setProgress(0, 0, true)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(this).notify(K.APP_UPDATE_NOTIFICATION_ID, notification)
    }

    private fun showDownloadedNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, K.APP_UPDATE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_download)
            .setColor(getColor(R.color.colorPrimary))
            .setContentTitle(getString(R.string.app_update_downloaded))
            .setProgress(0, 0, false)
            .setAutoCancel(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(this).notify(K.APP_UPDATE_NOTIFICATION_ID, notification)
    }

    private fun showDownloadErrorNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, K.APP_UPDATE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_download)
            .setColor(getColor(R.color.colorPrimary))
            .setContentTitle(getString(R.string.app_update_canceled))
            .setProgress(0, 0, false)
            .setAutoCancel(true)
            .setOngoing(false)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(this).notify(K.APP_UPDATE_NOTIFICATION_ID, notification)
    }
}