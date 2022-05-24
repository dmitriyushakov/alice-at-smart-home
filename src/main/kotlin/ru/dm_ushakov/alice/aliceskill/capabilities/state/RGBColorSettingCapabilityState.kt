package ru.dm_ushakov.alice.aliceskill.capabilities.state

import ru.dm_ushakov.alice.aliceskill.error.invalidValue

private fun packRGB(red: Int, green: Int, blue: Int): Int {
    if (red !in 0x0 .. 0xff) invalidValue("Red component should be in interval from 0 to 255! Actual - $red.")
    if (green !in 0x0 .. 0xff) invalidValue("Red component should be in interval from 0 to 255! Actual - $green.")
    if (blue !in 0x0 .. 0xff) invalidValue("Red component should be in interval from 0 to 255! Actual - $blue.")

    return (red shl 16) or (green shl 8) or blue
}

data class RGBColorSettingCapabilityState(val value: Int): BaseColorSettingCapabilityState() {
    override val instance: String get() = "rgb"

    val red: Int get() = (value and 0xff0000) shr 16
    val green: Int get() = (value and 0xff00) shr 8
    val blue: Int get() = value and 0xff

    constructor(red: Int, green: Int, blue: Int) : this(packRGB(red, green, blue))

    init {
        if (value !in 0x0..0xffffff) invalidValue("RGB value should be in interval from 0 to 16777215! Actual - $value.")
    }
}