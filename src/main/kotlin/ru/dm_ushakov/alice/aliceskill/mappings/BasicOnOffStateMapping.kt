package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.OnOffCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.OnOffCapabilityState

class BasicOnOffStateMapping(
    val onString: String,
    val offString: String
): StateMapping<OnOffCapabilityState, OnOffCapability> {
    override fun convertFromState(state: OnOffCapabilityState, capability: OnOffCapability): String {
        return if (state.value) onString else offString
    }

    override fun convertToState(stateString: String, capability: OnOffCapability): OnOffCapabilityState {
        return OnOffCapabilityState(stateString != offString)
    }
}