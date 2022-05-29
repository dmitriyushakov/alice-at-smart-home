package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ModeFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ModeCapabilityState
import ru.dm_ushakov.alice.aliceskill.devices.DeviceContentKey
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType.*
import ru.dm_ushakov.alice.aliceskill.error.deviceError
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class ModeCapability: BaseCapability() {
    final override val type: String get() = "devices.capabilities.mode"
    final override val keys: List<DeviceContentKey> get() = listOf(DeviceContentKey(type, instance.modeFunctionName))

    abstract val instance: ModeFunction
    abstract val modes: List<Mode>
    abstract var state: ModeCapabilityState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            val modesList = modes.toList()
            if (modesList.isEmpty()) error("Modes list should have at least 1 element!")

            put("instance", instance.modeFunctionName)

            putArray("modes", modesList) { mode ->
                addObject {
                    put("value", mode.modeName)
                }
            }
        }
    }

    final override fun getStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            put("instance", instance.modeFunctionName)
            put("value", state.value.modeName)
        }
    }

    final override fun executeChangingCapabilityState(changeState: JsonNode) {
        val incomeState = changeState["state"]

        val incomeInstance = ModeFunction.getModeFunction(incomeState["instance"].asText())
        val incomeValue = Mode.getMode(changeState["value"].asText())

        if (incomeInstance != instance) deviceError(InvalidAction, "This capability accept $instance instance. Got $incomeInstance.")

        if (incomeValue != state.value) {
            state = ModeCapabilityState(incomeInstance, incomeValue)
        }
    }
}