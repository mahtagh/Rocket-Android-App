package com.neofinancial.neo.android.interview.screens.next.launch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neofinancial.neo.android.interview.models.Launch
import com.neofinancial.neo.android.interview.network.LaunchesDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.Duration
import kotlinx.coroutines.delay
import java.time.Instant


class LaunchControlViewModel : ViewModel(){

    private val _nextLaunch = MutableStateFlow<Launch?>(null)
    val nextLaunch: StateFlow<Launch?> = _nextLaunch

    private val _countdown = MutableStateFlow("Calculating...")
    val countdown: StateFlow<String> = _countdown

    private val _launchStarted = MutableStateFlow(false)  // The lift-off state
    val launchStarted: StateFlow<Boolean> = _launchStarted


    init {
        viewModelScope.launch {
            try {
                val launches = LaunchesDomain.getLaunches()
                val next = getNextLaunch(launches)
                _nextLaunch.value = next

                next?.let {
                    val launchTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ZonedDateTime.parse(it.launchDate).toInstant()
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    //val simulatedLaunchTime = Instant.now().plusSeconds(5)
                    while (true) {
                        val now = Instant.now().minusSeconds(2 * 365 * 24 * 60 * 60)
                        //java.time.Instant.now()
                        val duration = Duration.between(now, launchTime)
                        if (duration.isNegative || duration.isZero) {
                            _countdown.value = "Launched!"
                            break
                        }
                        _countdown.value = formatDuration(duration)
                        delay(1000) // update every second
                    }
                }
            } catch (e: Exception) {
                _countdown.value = "Failed to load countdown"
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDuration(duration: Duration): String {
        val totalSeconds = duration.seconds
        val days = totalSeconds / (24 * 3600)
        val hours = (totalSeconds % (24 * 3600)) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
    }

    fun getNextLaunch(launches: List<Launch>): Launch? {
        return launches
            .mapNotNull {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        it to ZonedDateTime.parse(it.launchDate)
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                } catch (e: Exception) {
                    null
                }
            }
            .minByOrNull { it.second }
            ?.first
    }



}