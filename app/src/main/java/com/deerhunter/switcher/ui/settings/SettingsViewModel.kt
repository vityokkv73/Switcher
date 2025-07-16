package com.deerhunter.switcher.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deerhunter.switcher.preferences.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState("", "", ""))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    private val _actions = Channel<SettingsAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()
    
    init {
        viewModelScope.launch {
            val username = preferences.getString("username")
            val password = preferences.getString("password")
            val ipAddress = preferences.getString("ipAddress")
            
            _uiState.value = SettingsUiState(username, password, ipAddress)
        }
    }

    fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.UpdateUsername -> {
                _uiState.value = _uiState.value.copy(username = intent.username)
            }
            is SettingsIntent.UpdatePassword -> {
                _uiState.value = _uiState.value.copy(password = intent.password)
            }
            is SettingsIntent.UpdateIpAddress -> {
                _uiState.value = _uiState.value.copy(ipAddress = intent.ipAddress)
            }
            is SettingsIntent.UpdateSettings -> {
                viewModelScope.launch {
                    val currentState = _uiState.value
                    preferences.saveString("username", currentState.username)
                    preferences.saveString("password", currentState.password)
                    preferences.saveString("ipAddress", currentState.ipAddress)
                    
                    _actions.send(SettingsAction.ShowSuccessToast)
                }
            }
        }
    }
}
