package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ModeFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ModeCapabilityState
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType.*
import ru.dm_ushakov.alice.aliceskill.error.deviceError
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class ModeCapability: BaseCapability() {
    final override val type: String get() = "devices.capabilities.mode"

    abstract val instance: ModeFunction
    abstract val modes: List<Mode>
    abstract var state: ModeCapabilityState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            put("instance", instance.modeFunctionName)
            putArray("modes") {
                val modesList = modes.toList()
                if (modesList.isEmpty()) error("Modes list should have at least 1 element!")

                for (mode in modesList) {
                    addObject {
                        put("value", mode.modeName)
                    }
                }
            }
        }
    }

    final override fun getCapabilityStateJson() = makeJsonObject {
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