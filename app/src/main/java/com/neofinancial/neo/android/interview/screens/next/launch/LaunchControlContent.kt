package com.neofinancial.neo.android.interview.screens.next.launch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment



@Composable
fun LaunchControlContent(viewModel: LaunchControlViewModel = viewModel()) {
    val nextLaunch by viewModel.nextLaunch.collectAsState()
    val countdown by viewModel.countdown.collectAsState()
    val launchStarted by viewModel.launchStarted.collectAsState()

    Scaffold(
        topBar = {
            Header(stringResource(R.string.launch_control_title))
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = paddingValues.calculateTopPadding()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Tile {
                    Text(stringResource(R.string.label_launcher_explanation))
                }
                Tile {
                    if (nextLaunch != null) {
                        Text(
                            text = stringResource(R.string.label_next_launch, countdown),
                            style = Typography.labelMedium
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            RocketLiftOffController(startLaunch = launchStarted)
                        }
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

@Composable
fun RocketLiftOffController(
    startLaunch: Boolean
) {
    val offsetY by animateDpAsState(
        targetValue = if (startLaunch) (-200).dp else 0.dp,
        animationSpec = tween(durationMillis = 2000),
        label = "LiftOff"
    )

    Box(
        Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rocket),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = offsetY)
        )

    }
}