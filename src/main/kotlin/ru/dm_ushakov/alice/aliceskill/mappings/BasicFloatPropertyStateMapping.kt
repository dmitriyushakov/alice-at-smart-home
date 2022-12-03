package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.properties.FloatProperty
import ru.dm_ushakov.alice.aliceskill.properties.state.FloatPropertyState

class BasicFloatPropertyStateMapping(
    val intRequired: Boolean,
    val pointRequired: Boolean): StateMapping<FloatPropertyState, FloatProperty> {
    private fun Double.isInteger(): Boolean = !isInfinite() && this % 1.0 == 0.0

    override fun convertFromState(state: FloatPropertyState, capability: FloatProperty): String {
        val stateValue = state.value
        return if (intRequired || !pointRequired && stateValue.isInteger()) {
            stateValue.toLong().toString()
        } else {
            stateValue.toString()
        }
    }

    override fun convertToState(stateString: String, capability: FloatProperty): FloatPropertyState {
        return FloatPropertyState(
            instance = capability.instance,
            value = stateString.toDouble()
        )
    }
}