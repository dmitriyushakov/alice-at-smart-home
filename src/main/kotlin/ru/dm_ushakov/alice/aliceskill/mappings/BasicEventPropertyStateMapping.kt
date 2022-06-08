package ru.dm_ushakov.alice.aliceskill.mappings

import ru.dm_ushakov.alice.aliceskill.properties.model.EventType
import ru.dm_ushakov.alice.aliceskill.properties.state.EventPropertyState

class BasicEventPropertyStateMapping: StateMapping<EventPropertyState, EventPropertyState> {
    override fun convertTo(state: EventPropertyState, capability: EventPropertyState): String {
        return state.value.eventTypeName
    }

    override fun convertTo(stateString: String, capability: EventPropertyState): EventPropertyState {
        return EventPropertyState(
            instance = capability.instance,
            value = EventType.getEventType(stateString)
        )
    }
}