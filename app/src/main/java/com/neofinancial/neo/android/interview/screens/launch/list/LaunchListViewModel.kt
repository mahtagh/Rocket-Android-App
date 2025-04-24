package com.neofinancial.neo.android.interview.screens.launch.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neofinancial.neo.android.interview.models.Launch
import com.neofinancial.neo.android.interview.network.LaunchesDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LaunchListViewModel : ViewModel() {

    private val _launches = MutableStateFlow<List<Launch>>(emptyList())
    val launches: StateFlow<List<Launch>> = _launches.asStateFlow()

    private val _selectedLaunch = MutableStateFlow<Launch?>(null)
    val selectedLaunch: StateFlow<Launch?> = _selectedLaunch.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private var currentPage = 1
    private var canLoadMore = true

    init {
        loadLaunches()
    }

    fun selectLaunch(launch: Launch) {
        _selectedLaunch.value = launch
    }

    fun refreshLaunches() {
        currentPage = 1
        canLoadMore = true
        _launches.value = emptyList()
        loadLaunches(isRefresh = true)
    }

    fun loadMoreLaunches() {
        if (canLoadMore && !_isLoadingMore.value) {
            currentPage++
            loadLaunches(isLoadMore = true)
        }
    }

    private fun loadLaunches(isRefresh: Boolean = false, isLoadMore: Boolean = false) {
        viewModelScope.launch {
            try {
                if (isRefresh) _isRefreshing.value = true
                if (isLoadMore) _isLoadingMore.value = true

                val response = LaunchesDomain.getLaunches(currentPage)
                _launches.update { currentLaunches ->
                    currentLaunches + response.result
                }
                canLoadMore = response.meta.total > _launches.value.size
            } catch (e: Exception) {
                println("Error loading launches: ${e.message}")
                canLoadMore = false
            } finally {
                _isRefreshing.value = false
                _isLoadingMore.value = false
            }
        }
    }
}