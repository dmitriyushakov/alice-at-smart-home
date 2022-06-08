package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.ColorSettingCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorModel.*
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Scene
import ru.dm_ushakov.alice.aliceskill.capabilities.state.*
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType
import ru.dm_ushakov.alice.aliceskill.error.deviceError

class BasicColorStateMapping: StateMapping<BaseColorSettingCapabilityState, ColorSettingCapability> {
    private fun String.toIntTripleOrNull(): Triple<Int, Int, Int>? {
        val parts = split(',')
        if (parts.size != 3) return null
        val (a,b,c) = parts.map { it.toIntOrNull() }
        if (a == null || b == null || c == null) return null
        return Triple(a, b, c)
    }

    override fun convertTo(state: BaseColorSettingCapabilityState, capability: ColorSettingCapability): String {
        return when(state) {
            is HSVColorSettingCapabilityState -> with(state) { "$hue,$saturation,$value" }
            is RGBColorSettingCapabilityState -> with(state) { "$red,$green,$blue" }
            is KelvinColorSettingCapabilityState -> state.value.toString()
            is SceneColorSettingCapabilityState -> state.value.sceneName
            else -> deviceError(DeviceErrorType.InvalidValue, "Unknown incoming color setting state type!")
        }
    }

    override fun convertTo(stateString: String, capability: ColorSettingCapability): BaseColorSettingCapabilityState {
        val tripleValues = stateString.toIntTripleOrNull()
        val colorModel = capability.colorModel

        return if (colorModel != null && tripleValues != null) {
            when(colorModel) {
                RGB -> tripleValues.let { RGBColorSettingCapabilityState(it.first, it.second, it.third) }
                HSV -> tripleValues.let { HSVColorSettingCapabilityState(it.first, it.second, it.third) }
            }
        } else {
            val intValue = stateString.toIntOrNull()

            if (intValue != null && capability.temperatureRange != null) {
                KelvinColorSettingCapabilityState(intValue)
            } else {
                SceneColorSettingCapabilityState(Scene.getScene(stateString))
            }
        }
    }
}