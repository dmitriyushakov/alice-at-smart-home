package ru.dm_ushakov.alice.aliceskill.capabilities

import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ModeFunction

abstract class ModeCapability: BaseCapability() {
    override val type: String get() = "devices.capabilities.mode"

    abstract val instance: ModeFunction
    abstract val modes: List<Mode>
    abstract var value: Mode
}