package ru.dm_ushakov.alice.aliceskill.capabilities

import ru.dm_ushakov.alice.aliceskill.capabilities.state.BaseColorSettingCapabilityState
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorModel
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorScene
import ru.dm_ushakov.alice.aliceskill.capabilities.model.TemperatureRange

abstract class ColorSettingCapability: BaseCapability() {
    override val type: String get() = "devices.capabilities.color_setting"
    abstract val colorModel: ColorModel?
    abstract val temperatureRange: TemperatureRange?
    abstract val colorScene: ColorScene?
    abstract var state: BaseColorSettingCapabilityState
}