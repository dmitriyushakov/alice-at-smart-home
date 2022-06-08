package ru.dm_ushakov.alice.aliceskill.capabilities.state

import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeFunction

data class RangeCapabilityState (
    val instance: RangeFunction,
    val value: Double,
    val relative: Boolean? = null
)