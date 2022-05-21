package ru.dm_ushakov.alice.aliceskill.capabilities.model

enum class RangeUnit(val rangeName: String) {
    Percent("unit.percent"),
    TemperatureCelsius("unit.temperature.celsius"),
    TemperatureKelvin("unit.temperature.kelvin");

    override fun toString() = rangeName

    companion object {
        private val nameToRangeUnit: Map<String, RangeUnit> = values().associateBy { it.rangeName }
        fun getRangeUnit(rangeName: String) = nameToRangeUnit[rangeName] ?: error("Unknown range unit name - $rangeName!")

        val isPercentPredicate: (RangeUnit?) -> Boolean = { it == Percent }
        val isTemperaturePredicate: (RangeUnit?) -> Boolean = { it == TemperatureCelsius || it == TemperatureKelvin }
        val isNoUnitPredicate: (RangeUnit?) -> Boolean = { it == null }
    }
}