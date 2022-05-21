package ru.dm_ushakov.alice.aliceskill.capabilities.state

import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ModeFunction

data class ModeCapabilityState (val instance: ModeFunction, val value: Mode)