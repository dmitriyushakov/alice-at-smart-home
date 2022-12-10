package ru.dm_ushakov.alice.aliceskill.capabilities.model

import com.fasterxml.jackson.annotation.JsonValue

enum class ToggleFunction(@get:JsonValue val functionName: String) {
    Backlight("backlight"),
    ControlsLocked("controls_locked"),
    Ionization("ionization"),
    KeepWarm("keep_warm"),
    Mute("mute"),
    Oscillation("oscillation"),
    Pause("pause");

    override fun toString() = functionName

    companion object {
        private val namesToFunctions: Map<String, ToggleFunction> = values().associateBy { it.functionName }
        fun getToggleFunction(functionName: String) = namesToFunctions[functionName] ?: error("Unknown toggle function - $functionName")
    }
}