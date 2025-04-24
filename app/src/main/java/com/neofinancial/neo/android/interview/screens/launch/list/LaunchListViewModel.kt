package com.neofinancial.neo.android.interview.screens.launch.list
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neofinancial.neo.android.interview.models.Launch
import com.neofinancial.neo.android.interview.network.LaunchesDomain
import com.neofinancial.neo.android.interview.screens.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaunchListViewModel : ViewModel() {

    private val _launches = MutableStateFlow<List<Launch>>(emptyList())
    val launches: StateFlow<List<Launch>> = _launches.asStateFlow()

    private val _selectedLaunch = MutableStateFlow<Launch?>(null)
    val selectedLaunch: StateFlow<Launch?> = _selectedLaunch.asStateFlow()

    private val _timeRemaining = MutableStateFlow(0L)
    val timeRemaining: StateFlow<Long> = _timeRemaining.asStateFlow()

    private val _countdownDisplay = MutableStateFlow("LAUNCHED")
    val countdownDisplay: StateFlow<String> = _countdownDisplay.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _launches.value = LaunchesDomain.getLaunches()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun setLaunches(launchList: List<Launch>) {
        _launches.value = launchList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectLaunch(launch: Launch) {
        _selectedLaunch.value = launch
        viewModelScope.launch {
            val time = Time() // Create an instance of Time
            viewModelScope.launch {
                time.startCountdown(
                    launchDateString = launch.launchDate,
                    countdownDisplay = _countdownDisplay,
                    timeRemaining = _timeRemaining
                )
            }

        }
    }
}
