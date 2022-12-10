package ru.dm_ushakov.alice.aliceskill.properties.model

import com.fasterxml.jackson.annotation.JsonValue
import ru.dm_ushakov.alice.aliceskill.error.invalidValue
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatUnit.*

private val allValuesPredicate: (Double) -> Boolean = { true }
private val greaterThanOrEqualZeroCheck: (Double) -> Boolean = { it >= 0.0 }
private val inPercentsInterval: (Double) -> Boolean = { it in 0.0 .. 100.0 }

enum class FloatFunction(@get:JsonValue val functionName: String, val defaultUnit: FloatUnit, val allowedUnits:List<FloatUnit>, val valueCheck: (Double) -> Boolean = greaterThanOrEqualZeroCheck) {
    Amperage("amperage", Ampere, listOf(Ampere)),
    BatteryLevel("battery_level", Percent, listOf(Percent), inPercentsInterval),
    Power("power", Watt, listOf(Watt)),
    Voltage("voltage", Volt, listOf(Volt)),
    CO2Level("co2_level", PPM, listOf(PPM)),
    Humidity("humidity", Percent, listOf(Percent), inPercentsInterval),
    Illumination("illumination", Lux, listOf(Lux)),
    PM1Density("pm1_density", McgM3, listOf(McgM3)),
    PM2_5Density("pm2.5_density", McgM3, listOf(McgM3)),
    PM10Density("pm10_density", McgM3, listOf(McgM3)),
    TVOC("tvoc", McgM3, listOf(McgM3)),
    Pressure("pressure", MmHg, listOf(Atm, Pascal, Bar, MmHg)),
    Temperature("temperature", Celsius, listOf(Celsius, Kelvin), allValuesPredicate),
    WaterLevel("water_level", Percent, listOf(Percent), inPercentsInterval);

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