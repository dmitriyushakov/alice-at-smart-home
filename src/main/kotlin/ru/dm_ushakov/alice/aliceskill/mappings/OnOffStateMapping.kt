package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.capabilities.OnOffCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.OnOffCapabilityState

interface OnOffStateMapping: StateMapping<OnOffCapabilityState, OnOffCapability>