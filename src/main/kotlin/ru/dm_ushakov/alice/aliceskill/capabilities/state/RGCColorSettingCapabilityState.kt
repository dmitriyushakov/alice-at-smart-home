package ru.dm_ushakov.alice.aliceskill.capabilities.state

private fun packRGB(red: Int, green: Int, blue: Int): Int {
    if (red !in 0x0 .. 0xff) error("Red component should be in interval from 0 to 255!")
    if (green !in 0x0 .. 0xff) error("Red component should be in interval from 0 to 255!")
    if (blue !in 0x0 .. 0xff) error("Red component should be in interval from 0 to 255!")

    return (red shl 16) or (green shl 8) or blue
}

data class RGCColorSettingCapabilityState(val value: Int): BaseColorSettingCapabilityState() {
    override val instance: String get() = "rgb"

    val red: Int get() = (value and 0xff0000) shr 16
    val green: Int get() = (value and 0xff00) shr 8
    val blue: Int get() = value and 0xff

    constructor(red: Int, green: Int, blue: Int) : this(packRGB(red, green, blue))

    init {
        if (value !in 0x0..0xffffff) error("RGB value should be in interval from 0 to 16777215!")
    }
}