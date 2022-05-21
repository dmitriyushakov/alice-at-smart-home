package ru.dm_ushakov.alice.aliceskill.capabilities.state

data class HSVColorSettingCapabilityState(
    val hue: Int,
    val saturation: Int,
    val value: Int
): BaseColorSettingCapabilityState() {
    override val instance: String get() = "hsv"

    init {
        if (hue !in 0..360) error("Hue should be in interval from 0 to 360!")
        if (saturation !in 0..100) error("Saturation should be in interval from 0 to 100!")
        if (value !in 0..100) error("Value should be in interval from 0 to 100!")
    }
}