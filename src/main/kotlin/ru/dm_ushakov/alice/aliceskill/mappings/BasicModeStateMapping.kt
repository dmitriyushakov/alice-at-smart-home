package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.ModeCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ModeCapabilityState

class BasicModeStateMapping: StateMapping<ModeCapabilityState, ModeCapability> {
    override fun convertTo(state: ModeCapabilityState, capability: ModeCapability): String {
        return state.value.modeName
    }

    override fun convertTo(stateString: String, capability: ModeCapability): ModeCapabilityState {
        return ModeCapabilityState(capability.instance, Mode.getMode(stateString))
    }
}