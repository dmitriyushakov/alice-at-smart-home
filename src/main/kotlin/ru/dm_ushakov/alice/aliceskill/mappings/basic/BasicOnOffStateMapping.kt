package ru.dm_ushakov.alice.aliceskill.mappings.basic

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.OnOffCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.OnOffCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.OnOffStateMapping

@ComponentName("basic")
class BasicOnOffStateMapping(
    val onString: String,
    val offString: String
): OnOffStateMapping {
    override fun convertFromState(state: OnOffCapabilityState, capability: OnOffCapability): String {
        return if (state.value) onString else offString
    }

    override fun convertToState(stateString: String, capability: OnOffCapability): OnOffCapabilityState {
        return OnOffCapabilityState(stateString != offString)
    }
}