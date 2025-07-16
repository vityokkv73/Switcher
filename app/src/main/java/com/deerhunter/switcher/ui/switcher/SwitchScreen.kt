package com.deerhunter.switcher.ui.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deerhunter.switcher.model.SwitchInfo

@Composable
fun SwitchScreen(
    viewModel: SwitchViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveEvents(viewModel)

    SwitchScreenContent(
        switchInfoList = uiState.switchInfoList,
        isInternetConnected = uiState.isInternetConnected,
        onCloseClick = { viewModel.handleIntent(SwitchIntent.CloseSwitch(it)) },
        onOpenClick = { viewModel.handleIntent(SwitchIntent.OpenSwitch(it)) },
        onSettingsClick = onSettingsClick
    )
}

@Composable
private fun SwitchScreenContent(
    switchInfoList: List<SwitchInfo>,
    isInternetConnected: Boolean,
    onCloseClick: (SwitchInfo) -> Unit,
    onOpenClick: (SwitchInfo) -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxSize()) {
            InternetConnectionAvailability(isInternetConnected)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            ) {
                items(switchInfoList) { switchInfo ->
                    SwitchInfoCard(
                        switchInfo = switchInfo,
                        onCloseClick = onCloseClick,
                        onOpenClick = onOpenClick
                    )
                }
            }
        }

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun ObserveEvents(viewModel: SwitchViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.handleIntent(SwitchIntent.VisibilityChanged(isVisible = true))
                }

                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.handleIntent(SwitchIntent.VisibilityChanged(isVisible = false))
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
