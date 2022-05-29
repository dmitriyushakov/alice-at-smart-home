package ru.dm_ushakov.alice.aliceskill.properties

import ru.dm_ushakov.alice.aliceskill.properties.model.FloatFunction
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatUnit
import ru.dm_ushakov.alice.aliceskill.properties.state.FloatPropertyState
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class FloatProperty: BaseProperty() {
    override val type: String get() = "devices.properties.float"

    abstract val instance: FloatFunction
    abstract val unit: FloatUnit?

    abstract val state: FloatPropertyState

    override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("properties") {
            put("instance", instance.functionName)
            unit?.let { put("unit", it.unitName) }
        }
    }

    override fun getStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            state.let { state ->
                put("instance", state.instance.functionName)
                put("value", state.value)
            }
        }
    }
}