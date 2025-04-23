package com.neofinancial.neo.android.interview.screens.next.launch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neofinancial.neo.android.interview.R
import com.neofinancial.neo.android.interview.components.Header
import com.neofinancial.neo.android.interview.ui.theme.Typography


@Composable
fun LaunchControlContent() {
    val viewModel: LaunchControlViewModel = viewModel()
    val nextLaunch by viewModel.nextLaunch.collectAsState()
    val countdown by viewModel.countdown.collectAsState()

    Scaffold(
        topBar = {
            Header(stringResource(R.string.launch_control_title))
        },
        content = { paddingValues ->
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Tile {
                    Text(stringResource(R.string.label_launcher_explanation))
                }
                Tile {
                    if (nextLaunch != null) {
                        Text(
                            text = stringResource(R.string.label_next_launch, nextLaunch!!.name),
                            style = Typography.labelMedium
                        )
                        Text(
                            text = "Countdown: $countdown",
                            style = Typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.rocket),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                        )
                    } else {
                        Text("Loading next launch...", style = Typography.labelMedium)
                    }
                }
            }
        }
    )
}





@Composable
private fun Tile(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        color = colorScheme.surfaceContainer,
        shape = RoundedCornerShape(10),
    ) {
        Column(Modifier.padding(16.dp)) {
            content()
        }
    }
}