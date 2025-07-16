package com.deerhunter.switcher.ui.settings

sealed class SettingsIntent {
    data class UpdateUsername(val username: String) : SettingsIntent()
    data class UpdatePassword(val password: String) : SettingsIntent()
    data class UpdateIpAddress(val ipAddress: String) : SettingsIntent()
    object UpdateSettings : SettingsIntent()
}
