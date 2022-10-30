package ru.dm_ushakov.alice.aliceskill.config

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.dm_ushakov.alice.aliceskill.devices.Device
import ru.dm_ushakov.alice.aliceskill.devices.Lifecycle
import ru.dm_ushakov.alice.aliceskill.util.json.makeJsonObject
import ru.dm_ushakov.alice.aliceskill.util.json.putArray

class UserHome (
    val userId: String,
    val authorizationKeys: List<String>,
    devices: List<Device>): Lifecycle {
    var devices: List<Device> = devices
        set(newDevices) {
            val oldDevicesSet = field.toSet()
            val newDevicesSet = newDevices.toSet()

            for (device in field) {
                if (device !in newDevicesSet) {
                    device.onDestroy()
                }
            }

            for (device in newDevices) {
                if (device !in oldDevicesSet) {
                    device.onCreate()
                }
            }

            field = newDevices.toList()
        }

    override fun onCreate() {
        for (device in devices) {
            device.onCreate()
        }
    }

    override fun onDestroy() {
        for (device in devices) {
            device.onDestroy()
        }
    }

    @JsonIgnore
    fun getDescriptionJson() = makeJsonObject {
        put("user_id", userId)
        putArray("devices", devices) {
            add(it.getDescriptionJson())
        }
    }

    @JsonIgnore
    fun getStateJson() = makeJsonObject {
        putArray("devices", devices) {
            add(it.getStateJson())
        }
    }

    fun changeCapabilityState(changeState: JsonNode): JsonNode {
        val devicesArr = changeState["devices"] as? ArrayNode
        return makeJsonObject {
            putArray("devices") {
                devicesArr?.forEach { devNode ->
                    val devObject = devNode as? ObjectNode
                    if (devObject != null) {
                        val devId = devObject["id"]?.asText()
                        val device = devices.firstOrNull { dev -> dev.id == devId }
                        if (device != null) {
                            val changeRes = device.changeCapabilityState(devObject)
                            add(changeRes)
                        }
                    }
                }
            }
        }
    }
}