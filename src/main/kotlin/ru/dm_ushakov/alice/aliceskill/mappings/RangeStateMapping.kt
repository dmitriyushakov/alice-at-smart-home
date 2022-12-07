package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.RangeCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.RangeCapabilityState

interface RangeStateMapping: StateMapping<RangeCapabilityState, RangeCapability>