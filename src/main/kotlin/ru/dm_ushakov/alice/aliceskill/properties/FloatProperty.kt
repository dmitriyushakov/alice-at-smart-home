package ru.dm_ushakov.alice.aliceskill.properties

import ru.dm_ushakov.alice.aliceskill.devices.DeviceContentKey
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatFunction
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatUnit
import ru.dm_ushakov.alice.aliceskill.properties.state.FloatPropertyState
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class FloatProperty: BaseProperty() {
    final override val type: String get() = "devices.properties.float"
    final override val keys: List<DeviceContentKey> get() = listOf(DeviceContentKey(type, instance.functionName))

    abstract val instance: FloatFunction
    abstract val unit: FloatUnit?

    abstract val state: FloatPropertyState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            put("instance", instance.functionName)
            put("unit", (unit ?: instance.defaultUnit).unitName)
        }
    }

    final override fun getStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            state.let { state ->
                put("instance", state.instance.functionName)
                put("value", state.value)
            }
        }
    }
}