package ru.dm_ushakov.alice.aliceskill.capabilities.model

enum class RangeFunction(val functionName: String, private val allowedUnits: (RangeUnit?) -> Boolean) {
    Brightness("brightness", RangeUnit.isPercentPredicate),
    Channel("channel", RangeUnit.isNoUnitPredicate),
    Humidity("humidity",RangeUnit.isPercentPredicate),
    Open("open", RangeUnit.isPercentPredicate),
    Temperature("temperature", RangeUnit.isTemperaturePredicate),
    Volume("volume", RangeUnit.isPercentPredicate);

    override fun toString() = functionName

    fun checkUnit(unit: RangeUnit?) = allowedUnits(unit)
    fun checkUnit(unitName: String?) = checkUnit(unitName?.let { RangeUnit.getRangeUnit(it) })

    companion object {
        private val nameToFunction: Map<String, RangeFunction> = values().associateBy { it.functionName }
        fun getRangeFunction(functionName: String) = nameToFunction[functionName] ?: error("Unknown range function - $functionName")
    }
}