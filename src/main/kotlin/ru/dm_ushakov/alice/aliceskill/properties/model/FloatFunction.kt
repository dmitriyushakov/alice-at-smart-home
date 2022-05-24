package ru.dm_ushakov.alice.aliceskill.properties.model

import ru.dm_ushakov.alice.aliceskill.error.invalidValue
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatUnit.*

private val allValuesPredicate: (Double) -> Boolean = { true }
private val greaterThanOrEqualZeroCheck: (Double) -> Boolean = { it >= 0.0 }
private val inPercentsInterval: (Double) -> Boolean = { it in 0.0 .. 100.0 }

enum class FloatFunction(val functionName: String, val allowedUnits:List<FloatUnit>, val valueCheck: (Double) -> Boolean = greaterThanOrEqualZeroCheck) {
    Amperage("amperage", listOf(Ampere)),
    BatteryLevel("battery_level", listOf(Percent), inPercentsInterval),
    Power("power", listOf(Watt)),
    Voltage("voltage", listOf(Volt)),
    CO2Level("co2_level", listOf(PPM)),
    Humidity("humidity", listOf(Percent), inPercentsInterval),
    Illumination("illumination", listOf(Lux)),
    PM1Density("pm1_density", listOf(McgM3)),
    PM2_5Density("pm2.5_density", listOf(McgM3)),
    PM10Density("pm10_density", listOf(McgM3)),
    TVOC("tvoc", listOf(McgM3)),
    Pressure("pressure", listOf(Atm, Pascal, Bar, MmHg)),
    Temperature("temperature", listOf(Celsius, Kelvin), allValuesPredicate),
    WaterLevel("water_level", listOf(Percent), inPercentsInterval);

    override fun toString() = functionName

    fun validateValue(checkingValue: Double) {
        if (!valueCheck(checkingValue)) {
            invalidValue("Float value don't pass range check! Function - $functionName, value - $checkingValue.")
        }
    }

    fun checkUnit(unit: FloatUnit) = allowedUnits.any { it == unit }
    fun checkUnit(unitName: String) = checkUnit(FloatUnit.getFloatUnit(unitName))

    companion object {
        private val nameToFunction: Map<String, FloatFunction> = values().associateBy { it.functionName }
        fun getFloatFunction(functionName: String) = nameToFunction[functionName] ?: error("Unknown float function name - $functionName!")
    }
}