package ru.dm_ushakov.alice.aliceskill.capabilities

import ru.dm_ushakov.alice.aliceskill.capabilities.state.OnOffCapabilityState

abstract class OnOffCapability: BaseCapability() {
    override val type: String get() = "devices.capabilities.on_off"
    abstract val split: Boolean
    abstract var state: OnOffCapabilityState
}