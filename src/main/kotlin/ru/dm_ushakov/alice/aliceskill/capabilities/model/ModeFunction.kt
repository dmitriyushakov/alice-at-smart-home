package ru.dm_ushakov.alice.aliceskill.capabilities.model

import com.fasterxml.jackson.annotation.JsonValue
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode.*

enum class ModeFunction(@get:JsonValue val modeFunctionName: String, val recommendedModes: List<Mode>) {
    CleanupMode("cleanup_mode", listOf(Auto, Eco, Express, Normal, Quiet)),
    CoffeeMode("coffee_mode", listOf(Americano, Cappuccino, DoubleEspresso, Espresso, Latte)),
    Dishwashing("dishwashing", listOf(Auto, Eco, Express, Glass, Intensive, PreRinse, Quiet)),
    FanSpeed("fan_speed", listOf(Auto, High, Low, Medium, Turbo)),
    Heat("heat", listOf(Auto, Max, Min, Normal, Turbo)),
    InputSource("input_source", listOf(One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten)),
    Program("program", listOf(Auto, Express, One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten)),
    Swing("swing", listOf(Auto, Horizontal, Stationary, Vertical)),
    TeaMode("tea_mode", listOf(BlackTea, FlowerTea, GreenTea, HerbalTea, OolongTea, PuerhTea, RedTea, WhiteTea)),
    Thermostat("thermostat", listOf(Auto, Cool, Dry, FanOnly, Mode.Heat, Preheat)),
    WorkSpeed("work_speed", listOf(Auto, Fast, Max, Medium, Min, Slow, Turbo));

    override fun toString() = modeFunctionName

    companion object {
        private val nameToModeFunction: Map<String, ModeFunction> = values().associateBy { it.modeFunctionName }
        fun getModeFunction(modeFunctionName: String) = nameToModeFunction[modeFunctionName] ?: error("Unknown mode function - $modeFunctionName!")
    }
}