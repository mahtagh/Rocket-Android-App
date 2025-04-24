package com.neofinancial.neo.android.interview.screens.launch.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neofinancial.neo.android.interview.R
import com.neofinancial.neo.android.interview.models.Launch

@Composable
fun LaunchListContent(
    onLaunchSelected: (Launch) -> Unit = {}
) {
    val viewModel = viewModel<LaunchListViewModel>()
    val launches by viewModel.launches.collectAsState()
    val selectedLaunch by viewModel.selectedLaunch.collectAsState()
    val countdownDisplay by viewModel.countdownDisplay.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()

    val headerTitle = stringResource(R.string.launch_list_title)
    val launchName = selectedLaunch?.name ?: ""

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = headerTitle,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    if (selectedLaunch != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "- $launchName",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                if (selectedLaunch != null) {
                    Text(
                        text = countdownDisplay,
                        color = if (timeRemaining <= 0)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary,
                        fontWeight = if (timeRemaining <= 0)
                            FontWeight.Bold
                        else
                            FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(launches) { index, launch ->
                    if (index == 0) {
                        HorizontalDivider()
                    }
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectLaunch(launch)
                                onLaunchSelected(launch)
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        Text(launch.name)
                        Text(launch.launchDate, color = MaterialTheme.colorScheme.secondary)
                    }
                    HorizontalDivider()
                }
            }
        }
    )
}
