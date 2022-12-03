package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.RangeCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.RangeCapabilityState

class BasicRangeStateMapping(
    val intRequired: Boolean,
    val pointRequired: Boolean): StateMapping<RangeCapabilityState, RangeCapability> {
    private fun Double.isInteger(): Boolean = !isInfinite() && this % 1.0 == 0.0

    override fun convertFromState(state: RangeCapabilityState, capability: RangeCapability): String {
        val stateValue = state.value
        return if (intRequired || !pointRequired && stateValue.isInteger()) {
            stateValue.toLong().toString()
        } else {
            stateValue.toString()
        }
    }

    override fun convertToState(stateString: String, capability: RangeCapability): RangeCapabilityState {
        return RangeCapabilityState(
            instance = capability.instance,
            value = stateString.toDouble()
        )
    }
}