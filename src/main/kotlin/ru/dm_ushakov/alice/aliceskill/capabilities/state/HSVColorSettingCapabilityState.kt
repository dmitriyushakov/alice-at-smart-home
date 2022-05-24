package ru.dm_ushakov.alice.aliceskill.capabilities.state

import ru.dm_ushakov.alice.aliceskill.error.invalidValue

data class HSVColorSettingCapabilityState(
    val hue: Int,
    val saturation: Int,
    val value: Int
): BaseColorSettingCapabilityState() {
    override val instance: String get() = "hsv"

    init {
        if (hue !in 0..360) invalidValue("Hue should be in interval from 0 to 360! Actual - $hue.")
        if (saturation !in 0..100) invalidValue("Saturation should be in interval from 0 to 100! Actual - $saturation.")
        if (value !in 0..100) invalidValue("Value should be in interval from 0 to 100! Actual - $value.")
    }
}