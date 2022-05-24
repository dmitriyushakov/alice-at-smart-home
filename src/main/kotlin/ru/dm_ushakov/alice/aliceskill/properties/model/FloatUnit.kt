package ru.dm_ushakov.alice.aliceskill.properties.model

enum class FloatUnit(val unitName: String) {
    Ampere("unit.ampere"),
    Watt("unit.watt"),
    Volt("unit.volt"),
    Percent("unit.percent"),
    PPM("unit.ppm"),
    McgM3("unit.density.mcg_m3"),
    Lux("unit.illumination.lux"),
    Atm("unit.pressure.atm"),
    Pascal("unit.pressure.pascal"),
    Bar("unit.pressure.bar"),
    MmHg("unit.pressure.mmhg"),
    Celsius("unit.temperature.celsius"),
    Kelvin("unit.temperature.kelvin");

    override fun toString() = unitName

    companion object {
        private val nameToUnit = values().associateBy { it.unitName }
        fun getFloatUnit(unitName: String) = nameToUnit[unitName] ?: error("Unknown unit name - $unitName!")
    }
}