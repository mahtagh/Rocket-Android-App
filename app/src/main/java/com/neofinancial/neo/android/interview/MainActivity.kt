package com.neofinancial.neo.android.interview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.neofinancial.neo.android.interview.screens.next.launch.LaunchControlViewModel
import com.neofinancial.neo.android.interview.ui.theme.RocketLaunchTheme

class MainActivity : ComponentActivity() {

    val launchControlViewModel: LaunchControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RocketLaunchTheme {
                MainAppContent(launchControlViewModel = launchControlViewModel)
            }
        }
    }
}
