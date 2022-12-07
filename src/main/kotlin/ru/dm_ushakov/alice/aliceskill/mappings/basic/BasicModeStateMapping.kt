package ru.dm_ushakov.alice.aliceskill.mappings.basic

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.ModeCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ModeCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.ModeStateMapping

@ComponentName("basic")
class BasicModeStateMapping: ModeStateMapping {
    override fun convertFromState(state: ModeCapabilityState, capability: ModeCapability): String {
        return state.value.modeName
    }

    override fun convertToState(stateString: String, capability: ModeCapability): ModeCapabilityState {
        return ModeCapabilityState(capability.instance, Mode.getMode(stateString))
    }
}