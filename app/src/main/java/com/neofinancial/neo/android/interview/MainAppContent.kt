package com.neofinancial.neo.android.interview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neofinancial.neo.android.interview.screens.launch.list.LaunchListContent
import com.neofinancial.neo.android.interview.screens.next.launch.LaunchControlContent
import com.neofinancial.neo.android.interview.screens.next.launch.LaunchControlViewModel

@Composable
fun MainAppContent(launchControlViewModel: LaunchControlViewModel) {
    val navController = rememberNavController()

    val launchControlKey = stringResource(R.string.tab_launch_control)
    val launchListKey = stringResource(R.string.tab_launch_list)

    var selectedTab by remember { mutableStateOf(launchControlKey) }
    val items = remember {
        listOf(
            launchControlKey,
            launchListKey,
        )
    }

    Column(Modifier.fillMaxSize()) {
        NavHost(
            navController,
            startDestination = selectedTab,
            Modifier.weight(1f),
        ) {
            composable(launchControlKey) {
                LaunchControlContent(viewModel = launchControlViewModel)
            }
            composable(launchListKey) {
                LaunchListContent(
                    navController = navController,
                    launchControlViewModel = launchControlViewModel,
                    onTabSelected = { newTab -> selectedTab = newTab } // Pass the callback
                )
            }
        }
        NavigationBar {
            items.forEach { screenName ->
                NavigationBarItem(
                    icon = {
                        when (screenName) {
                            launchControlKey -> Text("🚀")
                            launchListKey -> Text("📆")
                        }
                    },
                    label = { Text(screenName) },
                    selected = screenName == selectedTab,
                    onClick = { selectedTab = screenName })
            }
        }
    }
}