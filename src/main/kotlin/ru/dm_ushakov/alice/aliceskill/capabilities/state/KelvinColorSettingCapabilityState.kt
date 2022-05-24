package ru.dm_ushakov.alice.aliceskill.capabilities.state

import ru.dm_ushakov.alice.aliceskill.error.invalidValue

class KelvinColorSettingCapabilityState(val value: Int) : BaseColorSettingCapabilityState() {
    override val instance: String get() = "temperature_k"

    init {
        if (value < 0) invalidValue("Kelvin value should be at least no less than 0! Actual - $value.")
    }
}