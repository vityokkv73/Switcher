package com.deerhunter.switcher.ui.switcher

import com.deerhunter.switcher.model.SwitchInfo
import com.deerhunter.switcher.model.predefinedSwitches

data class SwitchUiState(
    val switchInfoList: List<SwitchInfo> = predefinedSwitches,
    val isInternetConnected: Boolean = true,
)
