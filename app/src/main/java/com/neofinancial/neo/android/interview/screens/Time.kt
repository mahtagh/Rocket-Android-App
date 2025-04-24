package com.neofinancial.neo.android.interview.screens

import com.neofinancial.neo.android.interview.models.Launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration
import java.util.Locale
import java.util.concurrent.TimeUnit

object Time {

    /**
     * Formats a Duration into a human-readable countdown string (T-DD:HH:MM:SS).
     */
    fun formatDuration(duration: Duration): String {
        val totalSeconds = duration.seconds
        val days = TimeUnit.SECONDS.toDays(totalSeconds)
        val hours = TimeUnit.SECONDS.toHours(totalSeconds % TimeUnit.DAYS.toSeconds(1))
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds % TimeUnit.HOURS.toSeconds(1))
        val seconds = totalSeconds % TimeUnit.MINUTES.toSeconds(1)

        return when {
            days > 0 -> String.format(
                Locale.getDefault(),
                "%d day%s, %d:%02d:%02d",
                days,
                if (days > 1) "s" else "",
                hours,
                minutes,
                seconds
            )

            hours > 0 -> String.format(
                Locale.getDefault(),
                "%d hour%s, %02d:%02d",
                hours,
                if (hours > 1) "s" else "",
                minutes,
                seconds
            )

            minutes > 0 -> String.format(
                Locale.getDefault(),
                "%d minute%s, %02d seconds",
                minutes,
                if (minutes > 1) "s" else "",
                seconds
            )

            else -> String.format(
                Locale.getDefault(),
                "%d second%s",
                seconds,
                if (seconds != 1L) "s" else ""
            )
        }
    }

    // Start the countdown and update the display every 200 milliseconds
    suspend fun startCountdown(
        launch: Launch,
        countdownDisplay: MutableStateFlow<String>,
        onLaunched: () -> Unit
    ) {
        var timeUntilLaunch = launch.timeUntilLaunch()
        if (timeUntilLaunch.isNegative) {
            countdownDisplay.value = "LAUNCHED"
            onLaunched()
            return
        }

        do {
            timeUntilLaunch = launch.timeUntilLaunch()

            countdownDisplay.value = if (timeUntilLaunch.isNegative || timeUntilLaunch.isZero) {
                "LAUNCHED"
                onLaunched()
                return
            } else {
                formatDuration(timeUntilLaunch)
            }

            delay(200)
        } while (true)
    }
}
