package ru.dm_ushakov.alice.aliceskill.properties.state

import ru.dm_ushakov.alice.aliceskill.properties.model.FloatFunction

data class FloatPropertyState (val instance: FloatFunction, val value: Double) {
    init {
        instance.validateValue(value)
    }
}