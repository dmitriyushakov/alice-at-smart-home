package ru.dm_ushakov.alice.aliceskill.capabilities

import ru.dm_ushakov.alice.aliceskill.capabilities.model.Range
import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeUnit
import ru.dm_ushakov.alice.aliceskill.capabilities.state.RangeCapabilityState

abstract class RangeCapability: BaseCapability() {
    override val type: String get() = "devices.capabilities.range"
    abstract val instance: RangeFunction
    abstract val unit: RangeUnit?
    abstract val range: Range?
    abstract var state: RangeCapabilityState
}