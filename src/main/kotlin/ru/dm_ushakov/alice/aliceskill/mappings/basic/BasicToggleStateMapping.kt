package ru.dm_ushakov.alice.aliceskill.mappings.basic

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.ToggleCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ToggleCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.ToggleStateMapping

@ComponentName("basic")
class BasicToggleStateMapping(
    val onString: String,
    val offString: String
): ToggleStateMapping {
    override fun convertFromState(state: ToggleCapabilityState, capability: ToggleCapability): String {
        return if (state.value) onString else offString
    }

    override fun convertToState(stateString: String, capability: ToggleCapability): ToggleCapabilityState {
        val stateValue = stateString != offString
        return ToggleCapabilityState(
            instance = capability.instance,
            value = stateValue
        )
    }
}