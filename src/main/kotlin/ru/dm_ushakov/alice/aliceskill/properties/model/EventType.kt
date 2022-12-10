package ru.dm_ushakov.alice.aliceskill.properties.model

import com.fasterxml.jackson.annotation.JsonValue

enum class EventType(@get:JsonValue val eventTypeName: String) {
    Tilt("tilt"),
    Fall("fall"),
    Vibration("vibration"),
    Opened("opened"),
    Closed("closed"),
    Click("click"),
    DoubleClick("double_click"),
    LongPress("long_press"),
    Detected("detected"),
    NotDetected("not_detected"),
    High("high"),
    Low("low"),
    Normal("normal"),
    Dry("dry"),
    Leak("leak");

    override fun toString() = eventTypeName

    companion object {
        private val nameToEventType: Map<String, EventType> = values().associateBy { it.eventTypeName }
        fun getEventType(eventTypeName: String) = nameToEventType[eventTypeName] ?: error("Unknown event type - $eventTypeName!")
    }
}