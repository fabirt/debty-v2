package com.fabirt.debty.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.fabirt.debty.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_App)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        // Not recreating the activity.
        if (savedInstanceState == null) {
            handleIntent()
        }
    }

    private fun handleIntent() {
        intent?.dataString?.let { data ->
            when (data) {
                "debty.movement" -> triggerNewMovementDeepLink()
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
}