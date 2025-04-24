package com.neofinancial.neo.android.interview.screens.launch.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.neofinancial.neo.android.interview.R
import com.neofinancial.neo.android.interview.components.Header
import com.neofinancial.neo.android.interview.screens.next.launch.LaunchControlViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LaunchListContent(
    navController: NavController,
    launchControlViewModel: LaunchControlViewModel = viewModel<LaunchControlViewModel>(),
    onTabSelected: (String) -> Unit
) {
    val viewModel = viewModel<LaunchListViewModel>()
    val launches by viewModel.launches.collectAsState()
    val selectedLaunch by viewModel.selectedLaunch.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val launchControlRoute = stringResource(R.string.tab_launch_control)

    val headerTitle = stringResource(R.string.launch_list_title)
    val launchName = selectedLaunch?.name ?: ""

    // Remember scroll state to detect end of list
    val listState = rememberLazyListState()

    // Setup pull-to-refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshLaunches() }
    )

    // Monitor scroll position to load more when reaching the end
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount

            if (totalItemsCount > 0) {
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleItem >= totalItemsCount - 2
            } else {
                false
            }
        }.collect { shouldLoadMore ->
            if (shouldLoadMore && !isLoadingMore && !isRefreshing) {
                viewModel.loadMoreLaunches()
            }
        }
    }

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.tab_launch_list),
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    state = listState,
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
                                    launchControlViewModel.selectNextLaunch(launch)
                                    // Navigate to the Launch Control screen
                                    navController.navigate(launchControlRoute)
                                    onTabSelected(launchControlRoute)
                                }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                        ) {
                            Text(launch.name)
                            val zonedDateTime = launch.getTimeInSystemTimeZone()
                            val formattedDateTime = zonedDateTime?.format(
                                DateTimeFormatter.ofLocalizedDateTime(
                                    FormatStyle.MEDIUM,
                                    FormatStyle.MEDIUM
                                )
                            ) ?: "N/A"
                            Text(
                                text = formattedDateTime,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        HorizontalDivider()
                    }

                    if (isLoadingMore) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                // Pull-to-refresh indicator
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    )
}