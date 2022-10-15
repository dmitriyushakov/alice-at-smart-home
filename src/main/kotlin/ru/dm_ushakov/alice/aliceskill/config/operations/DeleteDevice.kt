package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

class DeleteDevice(val userId: String, val deviceId: String): ConfigOperation() {
    override fun applyChanges(config: JsonNode) {
        val rootNode = (config as? ObjectNode) ?: error("Expected object as root node!")
        val homesArr = (rootNode["homes"] as? ArrayNode) ?: error("Expected array as homes node!")
        var homeFound = false

        for (childNode in homesArr) {
            val homeNode = (childNode as? ObjectNode) ?: error("Expected object node as home!")
            val userIdNode = homeNode["userId"]
            if (!userIdNode.isTextual) error("Expected userId string in home!")
            val curUserId = userIdNode.asText() as String

            if (userId == curUserId) {
                homeFound = true
                val devicesNode = (homeNode["devices"] as? ArrayNode) ?: error("Expected array as devices node!")
                var found = false

                for ((deviceIdx, device) in devicesNode.withIndex()) {
                    val deviceNode = (device as? ObjectNode) ?: error("Expected object node as device!")
                    val deviceIdNode = deviceNode["id"]
                    if (!deviceIdNode.isTextual) error("Expected id string in device!")
                    val curDeviceId = deviceIdNode.asText() as String

                    if (deviceId == curDeviceId) {
                        found = true
                        devicesNode.remove(deviceIdx)
                        break
                    }
                }

                if (!found) error("Device with id \"$deviceId\" not found!")

                break
            }
        }

        if (!homeFound) error("Home with userId \"$userId\" not found!")
    }
}