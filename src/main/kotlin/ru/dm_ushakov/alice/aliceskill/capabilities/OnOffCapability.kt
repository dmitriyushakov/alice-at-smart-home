package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.state.OnOffCapabilityState
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class OnOffCapability: BaseCapability() {
    final override val type: String get() = "devices.capabilities.on_off"
    abstract val split: Boolean
    abstract var state: OnOffCapabilityState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            put("split", split)
        }
    }

    final override fun getCapabilityStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            put("instance", "on")
            put("value", state.value)
        }
    }

    final override fun executeChangingCapabilityState(changeState: JsonNode) {
        val value = changeState["state"]["value"].booleanValue()

        if (value != state.value) {
            state = OnOffCapabilityState(value)
        }
    }
}