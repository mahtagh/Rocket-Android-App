package com.neofinancial.neo.android.interview.screens.launch.list

import LaunchListViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neofinancial.neo.android.interview.R
import com.neofinancial.neo.android.interview.components.Header

@Composable
fun LaunchListContent() {

    val viewModel = viewModel<LaunchListViewModel>()
    val launches by viewModel.launches.collectAsState()

    Scaffold(
        topBar = {
            Header(stringResource(R.string.launch_list_title))
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(launches) { index, launch ->
                    if (index == 0) {
                        HorizontalDivider()
                    }
                    Row(
                        Modifier
                            .background(colorScheme.surfaceContainer)
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    ) {
                        Text(launch.name)
                        Text(launch.launchDate, color = colorScheme.secondary)
                    }
                    HorizontalDivider()
                }
            }
        }
    )
}