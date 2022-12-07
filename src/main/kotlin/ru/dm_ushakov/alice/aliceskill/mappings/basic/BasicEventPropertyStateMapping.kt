package ru.dm_ushakov.alice.aliceskill.mappings.basic

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.mappings.EventPropertyStateMapping
import ru.dm_ushakov.alice.aliceskill.properties.EventProperty
import ru.dm_ushakov.alice.aliceskill.properties.model.EventType
import ru.dm_ushakov.alice.aliceskill.properties.state.EventPropertyState

@ComponentName("basic")
class BasicEventPropertyStateMapping: EventPropertyStateMapping {
    override fun convertFromState(state: EventPropertyState, capability: EventProperty): String {
        return state.value.eventTypeName
    }

    override fun convertToState(stateString: String, capability: EventProperty): EventPropertyState {
        return EventPropertyState(
            instance = capability.instance,
            value = EventType.getEventType(stateString)
        )
    }
}