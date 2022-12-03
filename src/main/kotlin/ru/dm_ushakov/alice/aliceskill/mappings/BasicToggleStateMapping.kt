package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.ToggleCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ToggleCapabilityState

class BasicToggleStateMapping(
    val onString: String,
    val offString: String
): StateMapping<ToggleCapabilityState, ToggleCapability> {
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