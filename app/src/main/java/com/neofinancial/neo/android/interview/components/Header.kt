package com.neofinancial.neo.android.interview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neofinancial.neo.android.interview.ui.theme.Typography


@Composable
fun Header(title: String, modifier: Modifier = Modifier) {
    Surface(
        tonalElevation = 8.dp, // Add a subtle shadow
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier.padding(bottom = 16.dp)) {
            Text(
                title,
                Modifier
                    .padding(16.dp)
                    .statusBarsPadding(),
                style = Typography.titleLarge
            )
        }
    }
}