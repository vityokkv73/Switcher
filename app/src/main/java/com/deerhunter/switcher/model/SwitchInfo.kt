package com.deerhunter.switcher.model

data class SwitchInfo(
    val name: String,
    val port: Int,
    val switchState: SwitchState
)

val predefinedSwitches = listOf(
    SwitchInfo(name = "60/1", port = 60008, switchState = SwitchState.UNKNOWN),
    SwitchInfo(name = "60/7", port = 60009, switchState = SwitchState.UNKNOWN),
    SwitchInfo(name = "58/4", port = 58008, switchState = SwitchState.UNKNOWN),
    SwitchInfo(name = "58/7", port = 58009, switchState = SwitchState.UNKNOWN),
)
