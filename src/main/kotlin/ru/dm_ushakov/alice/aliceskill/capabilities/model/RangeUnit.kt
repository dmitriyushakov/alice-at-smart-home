package ru.dm_ushakov.alice.aliceskill.capabilities.model

import com.fasterxml.jackson.annotation.JsonValue

enum class RangeUnit(@get:JsonValue val rangeUnitName: String) {
    Percent("unit.percent"),
    TemperatureCelsius("unit.temperature.celsius"),
    TemperatureKelvin("unit.temperature.kelvin");

    override fun toString() = rangeUnitName

    companion object {
        private val nameToRangeUnit: Map<String, RangeUnit> = values().associateBy { it.rangeUnitName }
        fun getRangeUnit(rangeName: String) = nameToRangeUnit[rangeName] ?: error("Unknown range unit name - $rangeName!")

        val isPercentPredicate: (RangeUnit?) -> Boolean = { it == Percent }
        val isTemperaturePredicate: (RangeUnit?) -> Boolean = { it == TemperatureCelsius || it == TemperatureKelvin }
        val isNoUnitPredicate: (RangeUnit?) -> Boolean = { it == null }
    }
}