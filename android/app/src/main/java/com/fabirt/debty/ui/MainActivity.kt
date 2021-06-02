package com.fabirt.debty.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.fabirt.debty.R
import com.fabirt.debty.constant.K
import com.fabirt.debty.util.RootViewDeferringInsetsCallback
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
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
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Android 11 Window insets animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val rootView = window.decorView.rootView

            val deferringInsetsListener = RootViewDeferringInsetsCallback(
                persistentInsetTypes = WindowInsets.Type.systemBars(),
                deferredInsetTypes = WindowInsets.Type.ime()
            )

            rootView.setWindowInsetsAnimationCallback(deferringInsetsListener)
            rootView.setOnApplyWindowInsetsListener(deferringInsetsListener)
        }

        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Not recreating the activity.
        if (savedInstanceState == null) {
            handleIntent()
        }

        checkUpdate()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { updateInfo ->
                if (updateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    appUpdateManager.completeUpdate()
                }

                if (updateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        updateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        APP_UPDATE_REQUEST_CODE
                    )
                }
            }
    }

    private fun handleIntent() {
        intent?.dataString?.let { data ->
            when (data) {
                K.SHORTCUT_DATA_MOVEMENT_ASSISTANT -> triggerNewMovementDeepLink()
            }
        }
        createMoneyTransfer()
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
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { updateInfo ->
                val availability = updateInfo.updateAvailability()
                if (availability == UpdateAvailability.UPDATE_AVAILABLE) {
                    startUpdateFlow(appUpdateManager, updateInfo)
                }
            }.addOnFailureListener { error ->
                Log.e(TAG, error.toString())
            }
    }

    private fun startUpdateFlow(updateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
        updateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.FLEXIBLE,
            this,
            APP_UPDATE_REQUEST_CODE
        )
    }

    private fun createMoneyTransfer() {
        val transferAmount = intent.extras?.getString("transferAmount")
        val transferOriginName = intent.extras?.getString("moneyTransferOriginName")
        val transferDestinationName = intent.extras?.getString("moneyTransferDestinationName")
        val transferMode = intent.extras?.getString("transferMode")
        Log.i(TAG, "Amount: $transferAmount | Origin: $transferOriginName | Destination: $transferDestinationName | Mode: $transferMode")
    }
}