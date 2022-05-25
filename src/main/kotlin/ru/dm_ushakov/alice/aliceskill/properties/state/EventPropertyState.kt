package ru.dm_ushakov.alice.aliceskill.properties.state

import ru.dm_ushakov.alice.aliceskill.properties.model.EventFunction
import ru.dm_ushakov.alice.aliceskill.properties.model.EventType

data class EventPropertyState (val instance: EventFunction, val value: EventType) {
    init {
        instance.validateEventType(value)
    }
}