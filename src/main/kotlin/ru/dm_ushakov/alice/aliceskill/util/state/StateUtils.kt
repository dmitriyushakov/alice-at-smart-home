package ru.dm_ushakov.alice.aliceskill.util.state

import ru.dm_ushakov.alice.aliceskill.capabilities.state.RangeCapabilityState

fun RangeCapabilityState.relativeFrom(other: RangeCapabilityState): RangeCapabilityState =
    if(relative == true) RangeCapabilityState(instance, other.value + value) else this