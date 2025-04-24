package com.neofinancial.neo.android.interview.screens.next.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neofinancial.neo.android.interview.models.Launch
import com.neofinancial.neo.android.interview.models.LaunchResponse
import com.neofinancial.neo.android.interview.network.LaunchesDomain
import com.neofinancial.neo.android.interview.screens.Time
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchControlViewModel : ViewModel() {

    private val _nextLaunch = MutableStateFlow<Launch?>(null)
    val nextLaunch: StateFlow<Launch?> = _nextLaunch

    private val _countdown = MutableStateFlow("Calculating...")
    val countdown: StateFlow<String> = _countdown

    private val _launchStarted = MutableStateFlow(false)
    val launchStarted: StateFlow<Boolean> = _launchStarted

    private var countdownJob: Job? = null


    init {
        loadInitialNextLaunch()
    }

    private fun loadInitialNextLaunch() {
        viewModelScope.launch {
            if (_nextLaunch.value == null) {
                try {
                    val launches = LaunchesDomain.getLaunches(1)
                    val next = getNextLaunch(launches)
                    _nextLaunch.value = next
                    startCountdownForLaunch(next)
                } catch (_: Exception) {
                    _countdown.value = "Failed to load countdown"
                }

            }
        }
    }

    fun selectNextLaunch(launch: Launch) {
        _nextLaunch.value = launch
        startCountdownForLaunch(launch)
    }

    private fun startCountdownForLaunch(launch: Launch?) {
        // Cancel any existing countdown job
        countdownJob?.cancel()
        _countdown.value = if (launch != null) "Calculating..." else "No launch selected"
        _launchStarted.value = false

        launch?.let {
            countdownJob = viewModelScope.launch {
                Time.startCountdown(
                    launch = it,
                    countdownDisplay = _countdown,
                    onLaunched = { onLaunch() }
                )
            }
        }
    }

    private fun onLaunch(){
        _launchStarted.value = true
        _countdown.value = "LAUNCHED"
    }

    private fun getNextLaunch(launches: LaunchResponse): Launch? {
        return launches.result
            .mapNotNull { launch ->
                launch.getTimeInSystemTimeZone()?.let { zonedDateTime ->
                    launch to zonedDateTime
                }
            }
            .minByOrNull { it.second }
            ?.first
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel() // Ensure the countdown coroutine is cancelled when the ViewModel is cleared
    }
}
