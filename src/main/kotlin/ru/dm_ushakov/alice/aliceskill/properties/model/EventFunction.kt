package ru.dm_ushakov.alice.aliceskill.properties.model

import ru.dm_ushakov.alice.aliceskill.error.invalidValue
import ru.dm_ushakov.alice.aliceskill.properties.model.EventType.*

enum class EventFunction(val functionName: String, val defaultEventType: EventType, val allowedEventTypes: List<EventType>) {
    Vibration("vibration", Tilt, listOf(Tilt, Fall, EventType.Vibration)),
    Open("open", Closed, listOf(Opened, Closed)),
    Button("button", Click, listOf(Click, DoubleClick, LongPress)),
    Motion("motion", NotDetected, listOf(Detected, NotDetected)),
    Smoke("smoke", NotDetected, listOf(Detected, NotDetected, High)),
    Gas("gas", NotDetected, listOf(Detected, NotDetected, High)),
    BatteryLevel("battery_level", Normal, listOf(Normal, Low)),
    WaterLevel("water_level", Normal, listOf(Normal, Low)),
    WaterLeak("water_leak", Dry, listOf(Dry, Leak));

    override fun toString() = functionName

    fun checkEventType(eventType: EventType) = allowedEventTypes.any { it == eventType }
    fun checkEventType(eventTypeName: String) = checkEventType(EventType.getEventType(eventTypeName))

    fun validateEventType(eventType: EventType) {
        if (!checkEventType(eventType)) invalidValue("Value not in allowed list - $eventType!")
    }

    fun validateEventType(eventTypeName: String) {
        try {
            if (!checkEventType(eventTypeName)) invalidValue("Value not in allowed list - $eventTypeName!")
        } catch (ex: IllegalStateException) {
            invalidValue("Unknown event type - $eventTypeName!", ex)
        }
    }

    companion object {
        private val nameToEventFunction: Map<String, EventFunction> = values().associateBy { it.functionName }
        fun getEventFunction(functionName: String) = nameToEventFunction[functionName] ?: error("Unknown event function - $functionName!")
    }
}