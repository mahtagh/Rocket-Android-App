package com.neofinancial.neo.android.interview.screens.next.launch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neofinancial.neo.android.interview.models.Launch
import com.neofinancial.neo.android.interview.network.LaunchesDomain
import com.neofinancial.neo.android.interview.screens.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class LaunchControlViewModel : ViewModel() {

    private val _nextLaunch = MutableStateFlow<Launch?>(null)
    val nextLaunch: StateFlow<Launch?> = _nextLaunch

    private val _countdown = MutableStateFlow("Calculating...")
    val countdown: StateFlow<String> = _countdown

    private val _launchStarted = MutableStateFlow(false)
    val launchStarted: StateFlow<Boolean> = _launchStarted

    init {
        viewModelScope.launch {
            try {
                val launches = LaunchesDomain.getLaunches()
                val next = getNextLaunch(launches)
                _nextLaunch.value = next

                next?.let {
                    val time = Time() // Create an instance of Time
                    viewModelScope.launch {
                        time.startCountdown(
                            launchDateString = it.launchDate,
                            countdownDisplay = _countdown
                        )
                    }

                }
            } catch (e: Exception) {
                _countdown.value = "Failed to load countdown"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNextLaunch(launches: List<Launch>): Launch? {
        return launches
            .mapNotNull {
                try {
                    it to java.time.ZonedDateTime.parse(it.launchDate)
                } catch (e: Exception) {
                    null
                }
            }
            .minByOrNull { it.second }
            ?.first
    }
}
