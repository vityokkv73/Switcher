package com.deerhunter.switcher.repository

import com.deerhunter.switcher.model.SwitchState
import com.deerhunter.switcher.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class SwitchRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getSwitchState(port: Int): SwitchState = runCatching {
        withContext(Dispatchers.IO) {
            val response = apiService.getSwitchState(port)
            when (response.data.relayA) {
                0 -> SwitchState.CLOSE
                1 -> SwitchState.OPEN
                else -> SwitchState.UNKNOWN
            }
        }
    }.getOrElse { SwitchState.UNKNOWN }

    suspend fun changeSwitchState(state: SwitchState, port: Int) = runCatching {
        withContext(Dispatchers.IO) {
            apiService.changeSwitchState(open = state == SwitchState.OPEN, port = port)
        }
    }
}
