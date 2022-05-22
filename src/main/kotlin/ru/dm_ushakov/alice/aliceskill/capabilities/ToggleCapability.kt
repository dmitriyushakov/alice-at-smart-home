package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ToggleCapabilityState
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ToggleFunction
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType.*
import ru.dm_ushakov.alice.aliceskill.error.deviceError
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class ToggleCapability: BaseCapability() {
    final override val type: String get() = "devices.capabilities.toggle"
    abstract val instance: ToggleFunction
    abstract var state: ToggleCapabilityState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            put("instance", instance.functionName)
        }
    }

    final override fun getCapabilityStateJson() = makeJsonObject {
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

        val incomeInstance = ToggleFunction.getToggleFunction(incomeState["instance"].textValue())
        val incomeValue = incomeState["value"].booleanValue()

        if (instance != incomeInstance) deviceError(InvalidAction, "This capability expect instance - $instance. Got - $incomeInstance.")

        state = ToggleCapabilityState(incomeInstance, incomeValue)
    }
}