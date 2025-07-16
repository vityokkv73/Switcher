package com.deerhunter.switcher.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update

@Singleton
class NetworkConnectivityMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val connectionFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connectionFlow.update { true }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                connectionFlow.update { false }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun observeConnected(): Flow<Boolean> = connectionFlow.asStateFlow().filterNotNull()
}
