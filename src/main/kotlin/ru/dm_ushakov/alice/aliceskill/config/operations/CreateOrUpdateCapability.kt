package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.dm_ushakov.alice.aliceskill.reflection.ComponentsRegistry

class CreateOrUpdateCapability(val userId: String, val deviceId: String, val capabilityData: JsonNode): ConfigOperation() {
    val capabilityType = getCapabilityType(capabilityData)

    private fun getCapabilityType(capabilityData: JsonNode): String {
        val capabilityType = capabilityData["type"].asText() as String
        val capabilityTypes = ComponentsRegistry.capClasses.keys

        if (capabilityType !in capabilityTypes) error("Not allowed capability type - $capabilityType!")

        return capabilityType
    }

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
                        val capabilitiesNode = (deviceNode["capabilities"] as? ArrayNode) ?: error("Expected array as capabilities node!")
                        foundDevice = true
                        var foundCapability = false

                        for ((capIdx, capability) in capabilitiesNode.withIndex()) {
                            val capabilityNode = (capability as? ObjectNode) ?: error("Expected object node as capability!")
                            val capabilityTypeNode = capabilityNode["type"]
                            if (!capabilityTypeNode.isTextual) error("Expected type string in capability!")
                            val capabilityNodeType = capabilityTypeNode.asText() as String

                            if(capabilityNodeType == capabilityType) {
                                foundCapability = true
                                capabilitiesNode[capIdx] = capabilityData
                                break
                            }
                        }

                        if (!foundCapability) {
                            capabilitiesNode.add(capabilityData)
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