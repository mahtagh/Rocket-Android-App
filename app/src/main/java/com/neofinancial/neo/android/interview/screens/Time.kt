package com.neofinancial.neo.android.interview.screens

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime

class Time {

    // Format the duration into a human-readable string
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDuration(duration: Duration): String {
        val totalSeconds = duration.seconds
        val days = totalSeconds / (24 * 3600)
        val hours = (totalSeconds % (24 * 3600)) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("T-%d:%02d:%02d:%02d", days, hours, minutes, seconds)
    }

    // Parse the launch date string into a ZonedDateTime object
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseLaunchDate(dateString: String): ZonedDateTime? =
        try {
            ZonedDateTime.parse(dateString)
        } catch (e: Exception) {
            null
        }

    // Start the countdown and update the display every second
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun startCountdown(
        launchDateString: String,
        countdownDisplay: MutableStateFlow<String>,
        timeRemaining: MutableStateFlow<Long>? = null
    ) {
        val launchTime = parseLaunchDate(launchDateString)?.toInstant()
        if (launchTime == null) {
            countdownDisplay.value = "LAUNCHED"
            return
        }

        // Continuously update the countdown display and time remaining
        while (true) {
            val now = Instant.now()
            val duration = Duration.between(now, launchTime)
            val remainingMillis = duration.toMillis().coerceAtLeast(0L)

            timeRemaining?.value = remainingMillis

            countdownDisplay.value = if (duration.isNegative || duration.isZero) {
                "LAUNCHED"
            } else {
                formatDuration(duration)
            }

            delay(1000) // Update the countdown every second
        }
    }
}
