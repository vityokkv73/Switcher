package com.deerhunter.switcher.ui.switcher

import com.deerhunter.switcher.model.SwitchInfo

sealed class SwitchIntent {
    data class OpenSwitch(val switchInfo: SwitchInfo) : SwitchIntent()
    data class CloseSwitch(val switchInfo: SwitchInfo) : SwitchIntent()
    data class VisibilityChanged(val isVisible: Boolean): SwitchIntent()
}
