package ru.dm_ushakov.alice.aliceskill.capabilities

import ru.dm_ushakov.alice.aliceskill.capabilities.state.ToggleCapabilityState
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ToggleFunction

abstract class ToggleCapability: BaseCapability() {
    override val type: String get() = "devices.capabilities.toggle"
    abstract val instance: ToggleFunction
    abstract var state: ToggleCapabilityState
}