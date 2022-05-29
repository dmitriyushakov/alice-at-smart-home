package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Range
import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeUnit
import ru.dm_ushakov.alice.aliceskill.capabilities.state.RangeCapabilityState
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType.*
import ru.dm_ushakov.alice.aliceskill.error.deviceError
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class RangeCapability: BaseCapability() {
    final override val type: String get() = "devices.capabilities.range"
    abstract val instance: RangeFunction
    abstract val unit: RangeUnit?
    abstract val randomAccess: Boolean?
    abstract val range: Range?
    abstract var state: RangeCapabilityState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            put("instance", instance.functionName)
            unit?.let { put("unit", it.rangeUnitName) }
            randomAccess?.let { put("random_access", randomAccess) }
            range?.let { range ->
                putObject("range") {
                    range.min?.let { put("min", it) }
                    range.max?.let { put("max", it) }
                    range.precision?.let { put("precision", it) }
                }
            }
        }
    }

    final override fun getStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            state.let {
                put("instance", it.instance.functionName)
                put("value", it.value)
            }
        }
    }

    final override fun executeChangingCapabilityState(changeState: JsonNode) {
        val incomeState = changeState["state"]

        val incomeInstance = RangeFunction.getRangeFunction(incomeState["instance"].asText())
        val incomeValue = incomeState["value"].doubleValue()

        if (instance != incomeInstance) deviceError(InvalidAction, "This capability expect instance - $instance. Got - $incomeValue.")

        if (incomeValue != state.value) {
            state = RangeCapabilityState(incomeInstance, incomeValue)
        }
    }
}