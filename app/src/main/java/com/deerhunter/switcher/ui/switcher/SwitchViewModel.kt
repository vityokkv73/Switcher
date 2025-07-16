package com.deerhunter.switcher.ui.switcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deerhunter.switcher.model.SwitchInfo
import com.deerhunter.switcher.model.SwitchState
import com.deerhunter.switcher.network.NetworkConnectivityMonitor
import com.deerhunter.switcher.repository.SwitchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SwitchViewModel @Inject constructor(
    private val switchRepository: SwitchRepository,
    private val networkConnectivityMonitor: NetworkConnectivityMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(SwitchUiState())
    val uiState: StateFlow<SwitchUiState> = _uiState.asStateFlow()
    
    private var pollingJob: Job? = null

    init {
        observeNetworkConnectivity()
    }

    private fun observeNetworkConnectivity() {
        networkConnectivityMonitor.observeConnected().onEach { isConnected ->
            _uiState.value = _uiState.value.copy(isInternetConnected = isConnected)
        }.launchIn(viewModelScope)
    }

    fun handleIntent(intent: SwitchIntent) {
        when (intent) {
            is SwitchIntent.OpenSwitch -> handleChangeSwitchState(SwitchState.OPEN, intent.switchInfo)
            is SwitchIntent.CloseSwitch -> handleChangeSwitchState(SwitchState.CLOSE, intent.switchInfo)
            is SwitchIntent.VisibilityChanged -> handleVisibilityChanged(intent.isVisible)
        }
    }

    private fun handleVisibilityChanged(isVisible: Boolean) {
        if (isVisible) {
            startPolling()
        } else {
            stopPolling()
        }
    }

    private fun handleChangeSwitchState(newState: SwitchState, switchInfo: SwitchInfo) = viewModelScope.launch {
        switchRepository.changeSwitchState(newState, switchInfo.port)
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                val switchStates = _uiState.value.switchInfoList.map {
                    async { it.name to switchRepository.getSwitchState(it.port) }
                }.awaitAll()

                _uiState.value = _uiState.value.copy(
                    switchInfoList = _uiState.value.switchInfoList.map { switchInfo ->
                        switchInfo.copy(switchState = switchStates.find { it.first == switchInfo.name }!!.second)
                    }
                )
                delay(2.seconds)
            }
        }
    }
    
    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }
}
