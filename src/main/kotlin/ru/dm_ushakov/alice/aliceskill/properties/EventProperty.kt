package ru.dm_ushakov.alice.aliceskill.properties

import ru.dm_ushakov.alice.aliceskill.error.invalidValue
import ru.dm_ushakov.alice.aliceskill.properties.model.EventFunction
import ru.dm_ushakov.alice.aliceskill.properties.model.EventType
import ru.dm_ushakov.alice.aliceskill.properties.state.EventPropertyState
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class EventProperty: BaseProperty() {
    override val type: String get() = "devices.properties.event"

    abstract val instance: EventFunction
    abstract val events: List<EventType>

    abstract val state: EventPropertyState

    override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("properties") {
            val instance = this@EventProperty.instance
            val events = this@EventProperty.events
            if (events.isEmpty()) invalidValue("Event property should have at least one event!")

            put("instance", instance.functionName)
            putArray("events", events) { event ->
                instance.validateEventType(event)
                addObject {
                    put("value", event.eventTypeName)
                }
            }
        }
    }

    override fun getCapabilityStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            state.let { state ->
                put("instance", state.instance.functionName)
                put("value", state.value.eventTypeName)
            }
        }
    }
}