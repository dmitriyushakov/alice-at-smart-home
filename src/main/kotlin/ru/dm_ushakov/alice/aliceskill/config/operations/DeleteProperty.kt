package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

class DeleteProperty(val userId: String, val deviceId: String, val propertyType: String): ConfigOperation() {
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
                var foundDevice = false

                for (device in devicesNode) {
                    val deviceNode = (device as? ObjectNode) ?: error("Expected object node as device!")
                    val deviceIdNode = deviceNode["id"]
                    if (!deviceIdNode.isTextual) error("Expected id string in device!")
                    val curDeviceId = deviceIdNode.asText() as String

                    if (deviceId == curDeviceId) {
                        val propertiesNode = (deviceNode["properties"] as? ArrayNode) ?: error("Expected array as properties node!")
                        foundDevice = true
                        var foundProperty = false

                        for ((propIdx, property) in propertiesNode.withIndex()) {
                            val propertyNode = (property as? ObjectNode) ?: error("Expected object node as property!")
                            val propertyTypeNode = propertyNode["type"]
                            if (!propertyTypeNode.isTextual) error("Expected type string in property!")
                            val propertyNodeType = propertyTypeNode.asText() as String

                            if(propertyNodeType == propertyType) {
                                foundProperty = true
                                propertiesNode.remove(propIdx)
                                break
                            }
                        }

                        if (!foundProperty) {
                            error("Property type \"$propertyType\" not found!")
                        }
                    }
                }

                if (!foundDevice) error("Device with id \"$deviceId\" not found!")

                break
            }
        }

        if (!homeFound) error("Home with userId \"$userId\" not found!")
    }
}