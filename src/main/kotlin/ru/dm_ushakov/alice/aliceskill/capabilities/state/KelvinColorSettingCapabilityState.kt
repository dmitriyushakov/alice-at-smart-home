package ru.dm_ushakov.alice.aliceskill.capabilities.state

class KelvinColorSettingCapabilityState(val value: Int) : BaseColorSettingCapabilityState() {
    override val instance: String get() = "temperature_k"

    init {
        if (value < 0) error("Kelvin value should be at least no less than 0!")
    }
}